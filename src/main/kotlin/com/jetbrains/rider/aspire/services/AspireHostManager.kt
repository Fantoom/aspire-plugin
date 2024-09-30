package com.jetbrains.rider.aspire.services

import com.intellij.execution.RunManagerListener
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.services.ServiceEventListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.diagnostic.trace
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rider.aspire.generated.AspireSessionHostModel
import com.jetbrains.rider.aspire.listeners.AspireSessionHostModelListener
import com.jetbrains.rider.aspire.run.AspireHostConfiguration
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.iterator
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

@Service(Service.Level.PROJECT)
class AspireHostManager(private val project: Project) : Disposable {
    companion object {
        fun getInstance(project: Project) = project.service<AspireHostManager>()

        private val LOG = logger<AspireHostManager>()
    }

    private val aspireHosts = ConcurrentHashMap<Path, AspireHost>()

    private val serviceEventPublisher = project.messageBus.syncPublisher(ServiceEventListener.TOPIC)

    fun getAspireHosts(): List<AspireHost> {
        val hosts = mutableListOf<AspireHost>()
        for (host in aspireHosts) {
            hosts.add(host.value)
        }
        return hosts
    }

    fun getAspireHost(aspireHostProjectPathString: String) = aspireHosts[Path(aspireHostProjectPathString)]

    fun addAspireHost(aspireHostProjectPath: Path) {
        if (aspireHosts.containsKey(aspireHostProjectPath)) return

        LOG.trace { "Adding a new Aspire host ${aspireHostProjectPath.absolutePathString()}" }

        val aspireHost = AspireHost(aspireHostProjectPath, project)
        Disposer.register(this, aspireHost)

        aspireHosts[aspireHostProjectPath] = aspireHost

        val event = ServiceEventListener.ServiceEvent.createEvent(
            ServiceEventListener.EventType.SERVICE_ADDED,
            aspireHost,
            AspireMainServiceViewContributor::class.java
        )
        serviceEventPublisher.handle(event)
    }

    fun removeAspireHost(aspireHostProjectPath: Path) {
        LOG.trace { "Removing the Aspire host ${aspireHostProjectPath.absolutePathString()}" }

        val aspireHost = aspireHosts.remove(aspireHostProjectPath) ?: return

        val event = ServiceEventListener.ServiceEvent.createEvent(
            ServiceEventListener.EventType.SERVICE_REMOVED,
            aspireHost,
            AspireMainServiceViewContributor::class.java
        )
        serviceEventPublisher.handle(event)

        Disposer.dispose(aspireHost)
    }

    fun addSessionHostModel(
        aspireHostProjectPath: Path,
        sessionHostModel: AspireSessionHostModel,
        lifetime: Lifetime
    ) {
        val aspireHost = aspireHosts[aspireHostProjectPath]
        if (aspireHost == null) {
            LOG.warn("Unable to find Aspire host ${aspireHostProjectPath.absolutePathString()}")
            return
        }

        aspireHost.addSessionHostModel(sessionHostModel, lifetime)
    }

    override fun dispose() {
    }

    class RunListener(private val project: Project) : RunManagerListener {
        override fun runConfigurationAdded(settings: RunnerAndConfigurationSettings) {
            val configuration = settings.configuration
            if (configuration !is AspireHostConfiguration) return
            val params = configuration.parameters
            getInstance(project).addAspireHost(Path(params.projectFilePath))
        }

        override fun runConfigurationRemoved(settings: RunnerAndConfigurationSettings) {
            val configuration = settings.configuration
            if (configuration !is AspireHostConfiguration) return
            val params = configuration.parameters
            getInstance(project).removeAspireHost(Path(params.projectFilePath))
        }
    }

    class SessionHostModelListener(private val project: Project) : AspireSessionHostModelListener {
        override fun modelCreated(
            aspireHostProjectPath: Path,
            sessionHostModel: AspireSessionHostModel,
            lifetime: Lifetime
        ) {
            getInstance(project).addSessionHostModel(aspireHostProjectPath, sessionHostModel, lifetime)
        }
    }
}