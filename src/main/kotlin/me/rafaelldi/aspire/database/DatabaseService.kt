package me.rafaelldi.aspire.database

import com.intellij.database.access.DatabaseCredentialsUi
import com.intellij.database.dataSource.LocalDataSource
import com.intellij.database.dataSource.LocalDataSourceManager
import com.intellij.database.util.DbImplUtil
import com.intellij.database.util.LoaderContext
import com.intellij.database.util.performAutoIntrospection
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.debug
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.platform.ide.progress.withBackgroundProgress
import com.intellij.util.application
import com.jetbrains.rider.plugins.appender.database.dialog.steps.shared.services.connection.ConnectionManager
import com.jetbrains.rider.plugins.appender.database.dialog.steps.shared.services.connection.TestConnectionExecutionResult
import com.jetbrains.rider.plugins.appender.database.jdbcToConnectionString.converters.ConnectionStringToJdbcUrlConverter
import com.jetbrains.rider.plugins.appender.database.jdbcToConnectionString.dataProviders.MySqlClientDataProvider
import com.jetbrains.rider.plugins.appender.database.jdbcToConnectionString.dataProviders.NpgsqlDataProvider
import com.jetbrains.rider.plugins.appender.database.jdbcToConnectionString.dataProviders.OracleClientDataProvider
import com.jetbrains.rider.plugins.appender.database.jdbcToConnectionString.dataProviders.SqlClientDataProvider
import com.jetbrains.rider.plugins.appender.database.jdbcToConnectionString.factories.ConnectionStringsFactory
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import me.rafaelldi.aspire.settings.AspireSettings
import kotlin.time.Duration.Companion.milliseconds

@Service(Service.Level.PROJECT)
class DatabaseService(private val project: Project, scope: CoroutineScope) {
    companion object {
        fun getInstance(project: Project) = project.service<DatabaseService>()

        private val LOG = logger<DatabaseService>()

        private const val REDIS_CONNECTION_STRING_PATTERN =
            "(?<host>\\w*):(?<port>\\d*)(,user=(?<user>\\w*))?(,password=(?<password>\\w*))?"
        private val REDIS_REGEX = Regex(REDIS_CONNECTION_STRING_PATTERN)
    }

    private val rawConnectionStringTypes = listOf(
        DatabaseResourceType.MSSQL,
        DatabaseResourceType.MONGO
    )

    private val connectionManager = ConnectionManager(project)

    private val connectionStrings = mutableSetOf<DatabaseResourceConnectionString>()
    private val databaseResources = mutableSetOf<DatabaseResource>()
    private val createdConnections = mutableSetOf<String>()
    private val lock = Any()

    private val commands = MutableSharedFlow<DatabaseResourceCommand>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 100
    )

    private val createdDataSources = MutableSharedFlow<LocalDataSource>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 100
    )

    init {
        scope.launch {
            commands.collect { handleCommand(it) }
        }
        scope.launch {
            createdDataSources.collect { handleDataSource(it) }
        }
    }

    fun addConnectionString(connectionString: DatabaseResourceConnectionString) {
        if (!AspireSettings.getInstance().connectToDatabase) return

        connectionString.lifetime.bracketIfAlive({
            commands.tryEmit(AddConnectionStringCommand(connectionString))
        }, {
            commands.tryEmit(RemoveConnectionStringCommand(connectionString))
        })
    }

    fun addDatabaseResource(resource: DatabaseResource) {
        if (!AspireSettings.getInstance().connectToDatabase) return

        resource.lifetime.bracketIfAlive({
            commands.tryEmit(AddDatabaseResourceCommand(resource))
        }, {
            commands.tryEmit(RemoveDatabaseResourceCommand(resource))
        })
    }

    private suspend fun handleCommand(command: DatabaseResourceCommand) {
        when (command) {
            is AddConnectionStringCommand -> handleAddConnectionStringCommand(command)
            is RemoveConnectionStringCommand -> handleRemoveConnectionStringCommand(command)
            is AddDatabaseResourceCommand -> handleAddDatabaseResourceCommand(command)
            is RemoveDatabaseResourceCommand -> handleRemoveDatabaseResourceCommand(command)
        }
    }

    private suspend fun handleAddConnectionStringCommand(command: AddConnectionStringCommand) {
        val resource = findResourceByConnectionString(command.connectionString)
        if (resource != null) {
            createDataSource(command.connectionString, resource)
        } else {
            connectionStrings.add(command.connectionString)
        }
    }

    private fun findResourceByConnectionString(connectionString: DatabaseResourceConnectionString): DatabaseResource? {
        for (resource in databaseResources) {
            for (port in resource.ports) {
                if (connectionString.connectionString.contains(port)) {
                    return resource
                }
            }
        }

        return null
    }

    private suspend fun handleAddDatabaseResourceCommand(command: AddDatabaseResourceCommand) {
        val connectionString = findConnectionStringByResource(command.resource)
        if (connectionString != null) {
            createDataSource(connectionString, command.resource)
        } else {
            databaseResources.add(command.resource)
        }
    }

    private fun findConnectionStringByResource(resource: DatabaseResource): DatabaseResourceConnectionString? {
        for (connectionString in connectionStrings) {
            for (port in resource.ports) {
                if (connectionString.connectionString.contains(port)) {
                    return connectionString
                }
            }
        }

        return null
    }

    private suspend fun createDataSource(
        connectionString: DatabaseResourceConnectionString,
        resource: DatabaseResource
    ) {
        if (createdConnections.contains(connectionString.connectionString)) return

        val dataProvider = when (resource.type) {
            DatabaseResourceType.POSTGRES -> NpgsqlDataProvider.getInstance(project)
            DatabaseResourceType.MYSQL -> MySqlClientDataProvider.getInstance(project)
            DatabaseResourceType.MSSQL -> SqlClientDataProvider.getInstance(project)
            DatabaseResourceType.ORACLE -> OracleClientDataProvider.getInstance(project)
            DatabaseResourceType.MONGO -> DummyMongoDataProvider.getInstance(project)
            DatabaseResourceType.REDIS -> DummyRedisDataProvider.getInstance(project)
        }
        val driver = DbImplUtil.guessDatabaseDriver(dataProvider.dbms.first()) ?: return

        val url =
            if (resource.type == DatabaseResourceType.REDIS) {
                convertRedisConnectionString(connectionString.connectionString) ?: return
            } else if (rawConnectionStringTypes.contains(resource.type)) {
                connectionString.connectionString
            } else {
                val factory = ConnectionStringsFactory.get(dataProvider, project) ?: return
                val parsedConnectionString =
                    factory.create(connectionString.connectionString, dataProvider).getOrNull() ?: return
                ConnectionStringToJdbcUrlConverter.convert(parsedConnectionString, driver, project)
                    ?.build()
                    ?.getOrNull()
                    ?: return
            }

        val dataSource = LocalDataSource.fromDriver(driver, url, true).apply {
            name = connectionString.name
            isAutoSynchronize = true
        }
        val dataSourceManager = LocalDataSourceManager.getInstance(project)

        resource.lifetime.bracketIfAlive({
            synchronized(lock) {
                createdConnections.add(connectionString.connectionString)
            }
            application.invokeLater {
                dataSourceManager.addDataSource(dataSource)
            }
        }, {
            synchronized(lock) {
                createdConnections.remove(connectionString.connectionString)
            }
            application.invokeLater {
                dataSourceManager.removeDataSource(dataSource)
            }
        })

        createdDataSources.tryEmit(dataSource)
    }

    private fun convertRedisConnectionString(connectionString: String): String? {
        val matchResult = REDIS_REGEX.matchEntire(connectionString) ?: return null

        val host = matchResult.groups["host"]?.value
        val port = matchResult.groups["port"]?.value
        val user = matchResult.groups["user"]?.value
        val password = matchResult.groups["password"]?.value

        val sb = StringBuilder("jdbc:redis://")
        user?.let { sb.append(it).append(":") }
        password?.let { sb.append(it).append("@") }
        host?.let { sb.append(it) }
        port?.let { sb.append(":").append(it) }

        return sb.toString()
    }

    private fun handleRemoveConnectionStringCommand(command: RemoveConnectionStringCommand) {
        connectionStrings.remove(command.connectionString)
    }

    private fun handleRemoveDatabaseResourceCommand(command: RemoveDatabaseResourceCommand) {
        databaseResources.remove(command.resource)
    }

    private suspend fun handleDataSource(dataSource: LocalDataSource) =
        withBackgroundProgress(project, "Connecting to the database...") {
            val isConnectionSuccessful = waitForConnection(dataSource)
            if (!isConnectionSuccessful) {
                LOG.warn("Unable to connect to database")
                return@withBackgroundProgress
            }

            try {
                performAutoIntrospection(LoaderContext.selectGeneralTask(project, dataSource), true)
            } catch (ce: CancellationException) {
                LOG.trace("Introspection is canceled")
            } catch (e: Exception) {
                LOG.warn("Unable to perform auto introspection", e)
            }
        }

    private suspend fun waitForConnection(dataSource: LocalDataSource): Boolean {
        val credentials = DatabaseCredentialsUi.newUIInstance()

        for (i in 1..<5) {
            when (val connectionResult = connectionManager.testConnection(dataSource, credentials)) {
                TestConnectionExecutionResult.Cancelled -> {
                    LOG.debug("Connection cancelled")
                    return false
                }

                is TestConnectionExecutionResult.Failure -> {
                    LOG.debug { "Unable to connect to database, ${connectionResult.result.summary}" }
                    delay(300.milliseconds)
                }

                is TestConnectionExecutionResult.Success -> return true
            }
        }

        return false
    }

    private interface DatabaseResourceCommand

    private data class AddConnectionStringCommand(
        val connectionString: DatabaseResourceConnectionString
    ) : DatabaseResourceCommand

    private data class RemoveConnectionStringCommand(
        val connectionString: DatabaseResourceConnectionString
    ) : DatabaseResourceCommand

    private data class AddDatabaseResourceCommand(
        val resource: DatabaseResource
    ) : DatabaseResourceCommand

    private data class RemoveDatabaseResourceCommand(
        val resource: DatabaseResource
    ) : DatabaseResourceCommand
}

