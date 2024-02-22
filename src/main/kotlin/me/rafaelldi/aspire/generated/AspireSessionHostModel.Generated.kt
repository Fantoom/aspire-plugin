@file:Suppress("EXPERIMENTAL_API_USAGE","EXPERIMENTAL_UNSIGNED_LITERALS","PackageDirectoryMismatch","UnusedImport","unused","LocalVariableName","CanBeVal","PropertyName","EnumEntryName","ClassName","ObjectPropertyName","UnnecessaryVariable","SpellCheckingInspection")
package me.rafaelldi.aspire.generated

import com.jetbrains.rd.framework.*
import com.jetbrains.rd.framework.base.*
import com.jetbrains.rd.framework.impl.*

import com.jetbrains.rd.util.lifetime.*
import com.jetbrains.rd.util.reactive.*
import com.jetbrains.rd.util.string.*
import com.jetbrains.rd.util.*
import kotlin.time.Duration
import kotlin.reflect.KClass
import kotlin.jvm.JvmStatic



/**
 * #### Generated from [AspireSessionHostModel.kt:16]
 */
class AspireSessionHostModel private constructor(
    private val _sessions: RdMap<String, SessionModel>,
    private val _processStarted: RdSignal<ProcessStarted>,
    private val _processTerminated: RdSignal<ProcessTerminated>,
    private val _logReceived: RdSignal<LogReceived>,
    private val _resources: RdMap<String, ResourceModel>,
    private val _getTraceNodes: RdCall<Unit, Array<TraceNode>>
) : RdExtBase() {
    //companion
    
    companion object : ISerializersOwner {
        
        override fun registerSerializersCore(serializers: ISerializers)  {
            serializers.register(ProcessStarted)
            serializers.register(ProcessTerminated)
            serializers.register(LogReceived)
            serializers.register(SessionEnvironmentVariable)
            serializers.register(SessionModel)
            serializers.register(ResourceModel)
            serializers.register(ResourceProperty)
            serializers.register(ResourceEnvironmentVariable)
            serializers.register(ResourceEndpoint)
            serializers.register(ResourceService)
            serializers.register(ResourceLog)
            serializers.register(MetricKey)
            serializers.register(MetricValue)
            serializers.register(TraceNode)
            serializers.register(TraceNodeChild)
            serializers.register(TraceNodeAttribute)
            serializers.register(ResourceType.marshaller)
        }
        
        
        @JvmStatic
        @JvmName("internalCreateModel")
        @Deprecated("Use create instead", ReplaceWith("create(lifetime, protocol)"))
        internal fun createModel(lifetime: Lifetime, protocol: IProtocol): AspireSessionHostModel  {
            @Suppress("DEPRECATION")
            return create(lifetime, protocol)
        }
        
        @JvmStatic
        @Deprecated("Use protocol.aspireSessionHostModel or revise the extension scope instead", ReplaceWith("protocol.aspireSessionHostModel"))
        fun create(lifetime: Lifetime, protocol: IProtocol): AspireSessionHostModel  {
            AspireSessionHostRoot.register(protocol.serializers)
            
            return AspireSessionHostModel()
        }
        
        private val __TraceNodeArraySerializer = TraceNode.array()
        
        const val serializationHash = -3912791626157955055L
        
    }
    override val serializersOwner: ISerializersOwner get() = AspireSessionHostModel
    override val serializationHash: Long get() = AspireSessionHostModel.serializationHash
    
    //fields
    val sessions: IMutableViewableMap<String, SessionModel> get() = _sessions
    val processStarted: ISignal<ProcessStarted> get() = _processStarted
    val processTerminated: ISignal<ProcessTerminated> get() = _processTerminated
    val logReceived: ISignal<LogReceived> get() = _logReceived
    val resources: IMutableViewableMap<String, ResourceModel> get() = _resources
    val getTraceNodes: IRdCall<Unit, Array<TraceNode>> get() = _getTraceNodes
    //methods
    //initializer
    init {
        _sessions.optimizeNested = true
    }
    
    init {
        bindableChildren.add("sessions" to _sessions)
        bindableChildren.add("processStarted" to _processStarted)
        bindableChildren.add("processTerminated" to _processTerminated)
        bindableChildren.add("logReceived" to _logReceived)
        bindableChildren.add("resources" to _resources)
        bindableChildren.add("getTraceNodes" to _getTraceNodes)
    }
    
    //secondary constructor
    private constructor(
    ) : this(
        RdMap<String, SessionModel>(FrameworkMarshallers.String, SessionModel),
        RdSignal<ProcessStarted>(ProcessStarted),
        RdSignal<ProcessTerminated>(ProcessTerminated),
        RdSignal<LogReceived>(LogReceived),
        RdMap<String, ResourceModel>(FrameworkMarshallers.String, ResourceModel),
        RdCall<Unit, Array<TraceNode>>(FrameworkMarshallers.Void, __TraceNodeArraySerializer)
    )
    
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("AspireSessionHostModel (")
        printer.indent {
            print("sessions = "); _sessions.print(printer); println()
            print("processStarted = "); _processStarted.print(printer); println()
            print("processTerminated = "); _processTerminated.print(printer); println()
            print("logReceived = "); _logReceived.print(printer); println()
            print("resources = "); _resources.print(printer); println()
            print("getTraceNodes = "); _getTraceNodes.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): AspireSessionHostModel   {
        return AspireSessionHostModel(
            _sessions.deepClonePolymorphic(),
            _processStarted.deepClonePolymorphic(),
            _processTerminated.deepClonePolymorphic(),
            _logReceived.deepClonePolymorphic(),
            _resources.deepClonePolymorphic(),
            _getTraceNodes.deepClonePolymorphic()
        )
    }
    //contexts
    //threading
    override val extThreading: ExtThreadingKind get() = ExtThreadingKind.Default
}
val IProtocol.aspireSessionHostModel get() = getOrCreateExtension(AspireSessionHostModel::class) { @Suppress("DEPRECATION") AspireSessionHostModel.create(lifetime, this) }



/**
 * #### Generated from [AspireSessionHostModel.kt:27]
 */
data class LogReceived (
    val id: String,
    val isStdErr: Boolean,
    val message: String
) : IPrintable {
    //companion
    
    companion object : IMarshaller<LogReceived> {
        override val _type: KClass<LogReceived> = LogReceived::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): LogReceived  {
            val id = buffer.readString()
            val isStdErr = buffer.readBool()
            val message = buffer.readString()
            return LogReceived(id, isStdErr, message)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: LogReceived)  {
            buffer.writeString(value.id)
            buffer.writeBool(value.isStdErr)
            buffer.writeString(value.message)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as LogReceived
        
        if (id != other.id) return false
        if (isStdErr != other.isStdErr) return false
        if (message != other.message) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + id.hashCode()
        __r = __r*31 + isStdErr.hashCode()
        __r = __r*31 + message.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("LogReceived (")
        printer.indent {
            print("id = "); id.print(printer); println()
            print("isStdErr = "); isStdErr.print(printer); println()
            print("message = "); message.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
    //threading
}


/**
 * #### Generated from [AspireSessionHostModel.kt:94]
 */
data class MetricKey (
    val scope: String,
    val name: String
) : IPrintable {
    //companion
    
    companion object : IMarshaller<MetricKey> {
        override val _type: KClass<MetricKey> = MetricKey::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): MetricKey  {
            val scope = buffer.readString()
            val name = buffer.readString()
            return MetricKey(scope, name)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: MetricKey)  {
            buffer.writeString(value.scope)
            buffer.writeString(value.name)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as MetricKey
        
        if (scope != other.scope) return false
        if (name != other.name) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + scope.hashCode()
        __r = __r*31 + name.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("MetricKey (")
        printer.indent {
            print("scope = "); scope.print(printer); println()
            print("name = "); name.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
    //threading
}


/**
 * #### Generated from [AspireSessionHostModel.kt:99]
 */
data class MetricValue (
    val serviceName: String,
    val scope: String,
    val name: String,
    val description: String?,
    val unit: String?,
    val value: Double,
    val timestamp: Long
) : IPrintable {
    //companion
    
    companion object : IMarshaller<MetricValue> {
        override val _type: KClass<MetricValue> = MetricValue::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): MetricValue  {
            val serviceName = buffer.readString()
            val scope = buffer.readString()
            val name = buffer.readString()
            val description = buffer.readNullable { buffer.readString() }
            val unit = buffer.readNullable { buffer.readString() }
            val value = buffer.readDouble()
            val timestamp = buffer.readLong()
            return MetricValue(serviceName, scope, name, description, unit, value, timestamp)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: MetricValue)  {
            buffer.writeString(value.serviceName)
            buffer.writeString(value.scope)
            buffer.writeString(value.name)
            buffer.writeNullable(value.description) { buffer.writeString(it) }
            buffer.writeNullable(value.unit) { buffer.writeString(it) }
            buffer.writeDouble(value.value)
            buffer.writeLong(value.timestamp)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as MetricValue
        
        if (serviceName != other.serviceName) return false
        if (scope != other.scope) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (unit != other.unit) return false
        if (value != other.value) return false
        if (timestamp != other.timestamp) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + serviceName.hashCode()
        __r = __r*31 + scope.hashCode()
        __r = __r*31 + name.hashCode()
        __r = __r*31 + if (description != null) description.hashCode() else 0
        __r = __r*31 + if (unit != null) unit.hashCode() else 0
        __r = __r*31 + value.hashCode()
        __r = __r*31 + timestamp.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("MetricValue (")
        printer.indent {
            print("serviceName = "); serviceName.print(printer); println()
            print("scope = "); scope.print(printer); println()
            print("name = "); name.print(printer); println()
            print("description = "); description.print(printer); println()
            print("unit = "); unit.print(printer); println()
            print("value = "); value.print(printer); println()
            print("timestamp = "); timestamp.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
    //threading
}


/**
 * #### Generated from [AspireSessionHostModel.kt:17]
 */
data class ProcessStarted (
    val id: String,
    val pid: Long
) : IPrintable {
    //companion
    
    companion object : IMarshaller<ProcessStarted> {
        override val _type: KClass<ProcessStarted> = ProcessStarted::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): ProcessStarted  {
            val id = buffer.readString()
            val pid = buffer.readLong()
            return ProcessStarted(id, pid)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ProcessStarted)  {
            buffer.writeString(value.id)
            buffer.writeLong(value.pid)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as ProcessStarted
        
        if (id != other.id) return false
        if (pid != other.pid) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + id.hashCode()
        __r = __r*31 + pid.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("ProcessStarted (")
        printer.indent {
            print("id = "); id.print(printer); println()
            print("pid = "); pid.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
    //threading
}


/**
 * #### Generated from [AspireSessionHostModel.kt:22]
 */
data class ProcessTerminated (
    val id: String,
    val exitCode: Int
) : IPrintable {
    //companion
    
    companion object : IMarshaller<ProcessTerminated> {
        override val _type: KClass<ProcessTerminated> = ProcessTerminated::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): ProcessTerminated  {
            val id = buffer.readString()
            val exitCode = buffer.readInt()
            return ProcessTerminated(id, exitCode)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ProcessTerminated)  {
            buffer.writeString(value.id)
            buffer.writeInt(value.exitCode)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as ProcessTerminated
        
        if (id != other.id) return false
        if (exitCode != other.exitCode) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + id.hashCode()
        __r = __r*31 + exitCode.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("ProcessTerminated (")
        printer.indent {
            print("id = "); id.print(printer); println()
            print("exitCode = "); exitCode.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
    //threading
}


/**
 * #### Generated from [AspireSessionHostModel.kt:78]
 */
data class ResourceEndpoint (
    val endpointUrl: String,
    val proxyUrl: String
) : IPrintable {
    //companion
    
    companion object : IMarshaller<ResourceEndpoint> {
        override val _type: KClass<ResourceEndpoint> = ResourceEndpoint::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): ResourceEndpoint  {
            val endpointUrl = buffer.readString()
            val proxyUrl = buffer.readString()
            return ResourceEndpoint(endpointUrl, proxyUrl)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ResourceEndpoint)  {
            buffer.writeString(value.endpointUrl)
            buffer.writeString(value.proxyUrl)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as ResourceEndpoint
        
        if (endpointUrl != other.endpointUrl) return false
        if (proxyUrl != other.proxyUrl) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + endpointUrl.hashCode()
        __r = __r*31 + proxyUrl.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("ResourceEndpoint (")
        printer.indent {
            print("endpointUrl = "); endpointUrl.print(printer); println()
            print("proxyUrl = "); proxyUrl.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
    //threading
}


/**
 * #### Generated from [AspireSessionHostModel.kt:73]
 */
data class ResourceEnvironmentVariable (
    val key: String,
    val value: String?
) : IPrintable {
    //companion
    
    companion object : IMarshaller<ResourceEnvironmentVariable> {
        override val _type: KClass<ResourceEnvironmentVariable> = ResourceEnvironmentVariable::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): ResourceEnvironmentVariable  {
            val key = buffer.readString()
            val value = buffer.readNullable { buffer.readString() }
            return ResourceEnvironmentVariable(key, value)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ResourceEnvironmentVariable)  {
            buffer.writeString(value.key)
            buffer.writeNullable(value.value) { buffer.writeString(it) }
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as ResourceEnvironmentVariable
        
        if (key != other.key) return false
        if (value != other.value) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + key.hashCode()
        __r = __r*31 + if (value != null) value.hashCode() else 0
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("ResourceEnvironmentVariable (")
        printer.indent {
            print("key = "); key.print(printer); println()
            print("value = "); value.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
    //threading
}


/**
 * #### Generated from [AspireSessionHostModel.kt:89]
 */
data class ResourceLog (
    val text: String,
    val isError: Boolean?
) : IPrintable {
    //companion
    
    companion object : IMarshaller<ResourceLog> {
        override val _type: KClass<ResourceLog> = ResourceLog::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): ResourceLog  {
            val text = buffer.readString()
            val isError = buffer.readNullable { buffer.readBool() }
            return ResourceLog(text, isError)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ResourceLog)  {
            buffer.writeString(value.text)
            buffer.writeNullable(value.isError) { buffer.writeBool(it) }
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as ResourceLog
        
        if (text != other.text) return false
        if (isError != other.isError) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + text.hashCode()
        __r = __r*31 + if (isError != null) isError.hashCode() else 0
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("ResourceLog (")
        printer.indent {
            print("text = "); text.print(printer); println()
            print("isError = "); isError.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
    //threading
}


/**
 * #### Generated from [AspireSessionHostModel.kt:46]
 */
class ResourceModel private constructor(
    val name: String,
    val resourceType: ResourceType,
    val displayName: String,
    val uid: String,
    val state: String?,
    val createdAt: Date,
    val expectedEndpointsCount: Int?,
    val properties: Array<ResourceProperty>,
    val environment: Array<ResourceEnvironmentVariable>,
    val endpoints: Array<ResourceEndpoint>,
    val services: Array<ResourceService>,
    private val _logReceived: RdSignal<ResourceLog>
) : RdBindableBase() {
    //companion
    
    companion object : IMarshaller<ResourceModel> {
        override val _type: KClass<ResourceModel> = ResourceModel::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): ResourceModel  {
            val _id = RdId.read(buffer)
            val name = buffer.readString()
            val resourceType = buffer.readEnum<ResourceType>()
            val displayName = buffer.readString()
            val uid = buffer.readString()
            val state = buffer.readNullable { buffer.readString() }
            val createdAt = buffer.readDateTime()
            val expectedEndpointsCount = buffer.readNullable { buffer.readInt() }
            val properties = buffer.readArray {ResourceProperty.read(ctx, buffer)}
            val environment = buffer.readArray {ResourceEnvironmentVariable.read(ctx, buffer)}
            val endpoints = buffer.readArray {ResourceEndpoint.read(ctx, buffer)}
            val services = buffer.readArray {ResourceService.read(ctx, buffer)}
            val _logReceived = RdSignal.read(ctx, buffer, ResourceLog)
            return ResourceModel(name, resourceType, displayName, uid, state, createdAt, expectedEndpointsCount, properties, environment, endpoints, services, _logReceived).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ResourceModel)  {
            value.rdid.write(buffer)
            buffer.writeString(value.name)
            buffer.writeEnum(value.resourceType)
            buffer.writeString(value.displayName)
            buffer.writeString(value.uid)
            buffer.writeNullable(value.state) { buffer.writeString(it) }
            buffer.writeDateTime(value.createdAt)
            buffer.writeNullable(value.expectedEndpointsCount) { buffer.writeInt(it) }
            buffer.writeArray(value.properties) { ResourceProperty.write(ctx, buffer, it) }
            buffer.writeArray(value.environment) { ResourceEnvironmentVariable.write(ctx, buffer, it) }
            buffer.writeArray(value.endpoints) { ResourceEndpoint.write(ctx, buffer, it) }
            buffer.writeArray(value.services) { ResourceService.write(ctx, buffer, it) }
            RdSignal.write(ctx, buffer, value._logReceived)
        }
        
        
    }
    //fields
    val logReceived: ISource<ResourceLog> get() = _logReceived
    //methods
    //initializer
    init {
        bindableChildren.add("logReceived" to _logReceived)
    }
    
    //secondary constructor
    constructor(
        name: String,
        resourceType: ResourceType,
        displayName: String,
        uid: String,
        state: String?,
        createdAt: Date,
        expectedEndpointsCount: Int?,
        properties: Array<ResourceProperty>,
        environment: Array<ResourceEnvironmentVariable>,
        endpoints: Array<ResourceEndpoint>,
        services: Array<ResourceService>
    ) : this(
        name,
        resourceType,
        displayName,
        uid,
        state,
        createdAt,
        expectedEndpointsCount,
        properties,
        environment,
        endpoints,
        services,
        RdSignal<ResourceLog>(ResourceLog)
    )
    
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("ResourceModel (")
        printer.indent {
            print("name = "); name.print(printer); println()
            print("resourceType = "); resourceType.print(printer); println()
            print("displayName = "); displayName.print(printer); println()
            print("uid = "); uid.print(printer); println()
            print("state = "); state.print(printer); println()
            print("createdAt = "); createdAt.print(printer); println()
            print("expectedEndpointsCount = "); expectedEndpointsCount.print(printer); println()
            print("properties = "); properties.print(printer); println()
            print("environment = "); environment.print(printer); println()
            print("endpoints = "); endpoints.print(printer); println()
            print("services = "); services.print(printer); println()
            print("logReceived = "); _logReceived.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): ResourceModel   {
        return ResourceModel(
            name,
            resourceType,
            displayName,
            uid,
            state,
            createdAt,
            expectedEndpointsCount,
            properties,
            environment,
            endpoints,
            services,
            _logReceived.deepClonePolymorphic()
        )
    }
    //contexts
    //threading
}


/**
 * #### Generated from [AspireSessionHostModel.kt:67]
 */
data class ResourceProperty (
    val name: String,
    val displayName: String?,
    val value: String?
) : IPrintable {
    //companion
    
    companion object : IMarshaller<ResourceProperty> {
        override val _type: KClass<ResourceProperty> = ResourceProperty::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): ResourceProperty  {
            val name = buffer.readString()
            val displayName = buffer.readNullable { buffer.readString() }
            val value = buffer.readNullable { buffer.readString() }
            return ResourceProperty(name, displayName, value)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ResourceProperty)  {
            buffer.writeString(value.name)
            buffer.writeNullable(value.displayName) { buffer.writeString(it) }
            buffer.writeNullable(value.value) { buffer.writeString(it) }
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as ResourceProperty
        
        if (name != other.name) return false
        if (displayName != other.displayName) return false
        if (value != other.value) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + name.hashCode()
        __r = __r*31 + if (displayName != null) displayName.hashCode() else 0
        __r = __r*31 + if (value != null) value.hashCode() else 0
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("ResourceProperty (")
        printer.indent {
            print("name = "); name.print(printer); println()
            print("displayName = "); displayName.print(printer); println()
            print("value = "); value.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
    //threading
}


/**
 * #### Generated from [AspireSessionHostModel.kt:83]
 */
data class ResourceService (
    val name: String,
    val allocatedAddress: String?,
    val allocatedPort: Int?
) : IPrintable {
    //companion
    
    companion object : IMarshaller<ResourceService> {
        override val _type: KClass<ResourceService> = ResourceService::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): ResourceService  {
            val name = buffer.readString()
            val allocatedAddress = buffer.readNullable { buffer.readString() }
            val allocatedPort = buffer.readNullable { buffer.readInt() }
            return ResourceService(name, allocatedAddress, allocatedPort)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ResourceService)  {
            buffer.writeString(value.name)
            buffer.writeNullable(value.allocatedAddress) { buffer.writeString(it) }
            buffer.writeNullable(value.allocatedPort) { buffer.writeInt(it) }
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as ResourceService
        
        if (name != other.name) return false
        if (allocatedAddress != other.allocatedAddress) return false
        if (allocatedPort != other.allocatedPort) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + name.hashCode()
        __r = __r*31 + if (allocatedAddress != null) allocatedAddress.hashCode() else 0
        __r = __r*31 + if (allocatedPort != null) allocatedPort.hashCode() else 0
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("ResourceService (")
        printer.indent {
            print("name = "); name.print(printer); println()
            print("allocatedAddress = "); allocatedAddress.print(printer); println()
            print("allocatedPort = "); allocatedPort.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
    //threading
}


/**
 * #### Generated from [AspireSessionHostModel.kt:48]
 */
enum class ResourceType {
    Project, 
    Container, 
    Executable, 
    Unknown;
    
    companion object {
        val marshaller = FrameworkMarshallers.enum<ResourceType>()
        
    }
}


/**
 * #### Generated from [AspireSessionHostModel.kt:33]
 */
data class SessionEnvironmentVariable (
    val key: String,
    val value: String
) : IPrintable {
    //companion
    
    companion object : IMarshaller<SessionEnvironmentVariable> {
        override val _type: KClass<SessionEnvironmentVariable> = SessionEnvironmentVariable::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): SessionEnvironmentVariable  {
            val key = buffer.readString()
            val value = buffer.readString()
            return SessionEnvironmentVariable(key, value)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: SessionEnvironmentVariable)  {
            buffer.writeString(value.key)
            buffer.writeString(value.value)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as SessionEnvironmentVariable
        
        if (key != other.key) return false
        if (value != other.value) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + key.hashCode()
        __r = __r*31 + value.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("SessionEnvironmentVariable (")
        printer.indent {
            print("key = "); key.print(printer); println()
            print("value = "); value.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
    //threading
}


/**
 * #### Generated from [AspireSessionHostModel.kt:38]
 */
data class SessionModel (
    val id: String,
    val projectPath: String,
    val debug: Boolean,
    val args: Array<String>?,
    val envs: Array<SessionEnvironmentVariable>?
) : IPrintable {
    //companion
    
    companion object : IMarshaller<SessionModel> {
        override val _type: KClass<SessionModel> = SessionModel::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): SessionModel  {
            val id = buffer.readString()
            val projectPath = buffer.readString()
            val debug = buffer.readBool()
            val args = buffer.readNullable { buffer.readArray {buffer.readString()} }
            val envs = buffer.readNullable { buffer.readArray {SessionEnvironmentVariable.read(ctx, buffer)} }
            return SessionModel(id, projectPath, debug, args, envs)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: SessionModel)  {
            buffer.writeString(value.id)
            buffer.writeString(value.projectPath)
            buffer.writeBool(value.debug)
            buffer.writeNullable(value.args) { buffer.writeArray(it) { buffer.writeString(it) } }
            buffer.writeNullable(value.envs) { buffer.writeArray(it) { SessionEnvironmentVariable.write(ctx, buffer, it) } }
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as SessionModel
        
        if (id != other.id) return false
        if (projectPath != other.projectPath) return false
        if (debug != other.debug) return false
        if (args != other.args) return false
        if (envs != other.envs) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + id.hashCode()
        __r = __r*31 + projectPath.hashCode()
        __r = __r*31 + debug.hashCode()
        __r = __r*31 + if (args != null) args.contentDeepHashCode() else 0
        __r = __r*31 + if (envs != null) envs.contentDeepHashCode() else 0
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("SessionModel (")
        printer.indent {
            print("id = "); id.print(printer); println()
            print("projectPath = "); projectPath.print(printer); println()
            print("debug = "); debug.print(printer); println()
            print("args = "); args.print(printer); println()
            print("envs = "); envs.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
    //threading
}


/**
 * #### Generated from [AspireSessionHostModel.kt:109]
 */
data class TraceNode (
    val id: String,
    val name: String,
    val serviceName: String?,
    val children: List<TraceNodeChild>,
    val attributes: List<TraceNodeAttribute>
) : IPrintable {
    //companion
    
    companion object : IMarshaller<TraceNode> {
        override val _type: KClass<TraceNode> = TraceNode::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): TraceNode  {
            val id = buffer.readString()
            val name = buffer.readString()
            val serviceName = buffer.readNullable { buffer.readString() }
            val children = buffer.readList { TraceNodeChild.read(ctx, buffer) }
            val attributes = buffer.readList { TraceNodeAttribute.read(ctx, buffer) }
            return TraceNode(id, name, serviceName, children, attributes)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: TraceNode)  {
            buffer.writeString(value.id)
            buffer.writeString(value.name)
            buffer.writeNullable(value.serviceName) { buffer.writeString(it) }
            buffer.writeList(value.children) { v -> TraceNodeChild.write(ctx, buffer, v) }
            buffer.writeList(value.attributes) { v -> TraceNodeAttribute.write(ctx, buffer, v) }
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as TraceNode
        
        if (id != other.id) return false
        if (name != other.name) return false
        if (serviceName != other.serviceName) return false
        if (children != other.children) return false
        if (attributes != other.attributes) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + id.hashCode()
        __r = __r*31 + name.hashCode()
        __r = __r*31 + if (serviceName != null) serviceName.hashCode() else 0
        __r = __r*31 + children.hashCode()
        __r = __r*31 + attributes.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("TraceNode (")
        printer.indent {
            print("id = "); id.print(printer); println()
            print("name = "); name.print(printer); println()
            print("serviceName = "); serviceName.print(printer); println()
            print("children = "); children.print(printer); println()
            print("attributes = "); attributes.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
    //threading
}


/**
 * #### Generated from [AspireSessionHostModel.kt:122]
 */
data class TraceNodeAttribute (
    val key: String,
    val value: String
) : IPrintable {
    //companion
    
    companion object : IMarshaller<TraceNodeAttribute> {
        override val _type: KClass<TraceNodeAttribute> = TraceNodeAttribute::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): TraceNodeAttribute  {
            val key = buffer.readString()
            val value = buffer.readString()
            return TraceNodeAttribute(key, value)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: TraceNodeAttribute)  {
            buffer.writeString(value.key)
            buffer.writeString(value.value)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as TraceNodeAttribute
        
        if (key != other.key) return false
        if (value != other.value) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + key.hashCode()
        __r = __r*31 + value.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("TraceNodeAttribute (")
        printer.indent {
            print("key = "); key.print(printer); println()
            print("value = "); value.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
    //threading
}


/**
 * #### Generated from [AspireSessionHostModel.kt:117]
 */
data class TraceNodeChild (
    val id: String,
    val connectionCount: Int
) : IPrintable {
    //companion
    
    companion object : IMarshaller<TraceNodeChild> {
        override val _type: KClass<TraceNodeChild> = TraceNodeChild::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): TraceNodeChild  {
            val id = buffer.readString()
            val connectionCount = buffer.readInt()
            return TraceNodeChild(id, connectionCount)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: TraceNodeChild)  {
            buffer.writeString(value.id)
            buffer.writeInt(value.connectionCount)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as TraceNodeChild
        
        if (id != other.id) return false
        if (connectionCount != other.connectionCount) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + id.hashCode()
        __r = __r*31 + connectionCount.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("TraceNodeChild (")
        printer.indent {
            print("id = "); id.print(printer); println()
            print("connectionCount = "); connectionCount.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
    //threading
}
