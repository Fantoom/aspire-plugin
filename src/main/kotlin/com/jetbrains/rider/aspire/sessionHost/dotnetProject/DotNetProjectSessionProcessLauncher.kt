@file:Suppress("UnstableApiUsage")

package com.jetbrains.rider.aspire.sessionHost.dotnetProject

import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.ExecutionResult
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.ide.browsers.StartBrowserSettings
import com.intellij.openapi.application.EDT
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.diagnostic.trace
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rider.aspire.generated.SessionModel
import com.jetbrains.rider.aspire.run.AspireHostConfiguration
import com.jetbrains.rider.aspire.sessionHost.projectLaunchers.BaseProjectSessionProcessLauncher
import com.jetbrains.rider.debugger.createAndStartSession
import com.jetbrains.rider.run.ConsoleKind
import com.jetbrains.rider.run.IDebuggerOutputListener
import com.jetbrains.rider.run.createConsole
import com.jetbrains.rider.runtime.DotNetExecutable
import com.jetbrains.rider.runtime.DotNetRuntime
import com.jetbrains.rider.runtime.dotNetCore.DotNetCoreRuntime
import icons.RiderIcons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension

class DotNetProjectSessionProcessLauncher : BaseProjectSessionProcessLauncher() {
    companion object {
        private val LOG = logger<DotNetProjectSessionProcessLauncher>()
    }

    override val priority = 10

    override val hotReloadExtension = DotNetProjectHotReloadConfigurationExtension()

    override suspend fun isApplicable(projectPath: String, project: Project) = true

    override fun getRunProfile(
        sessionId: String,
        projectName: String,
        dotnetExecutable: DotNetExecutable,
        dotnetRuntime: DotNetRuntime,
        sessionProcessEventListener: ProcessListener,
        sessionProcessTerminatedListener: ProcessListener,
        sessionProcessLifetime: Lifetime
    ) = DotNetProjectSessionRunProfile(
        sessionId,
        projectName,
        dotnetExecutable,
        dotnetRuntime,
        sessionProcessEventListener,
        sessionProcessTerminatedListener,
        sessionProcessLifetime
    )

    override suspend fun launchDebugProcess(
        sessionId: String,
        sessionModel: SessionModel,
        sessionProcessEventListener: ProcessListener,
        sessionProcessTerminatedListener: ProcessListener,
        sessionProcessLifetime: Lifetime,
        hostRunConfiguration: AspireHostConfiguration?,
        project: Project
    ) {
        LOG.trace { "Starting debug session for project ${sessionModel.projectPath}" }

        val (executable, browserSettings) = getDotNetExecutable(sessionModel, hostRunConfiguration, true, project) ?: return
        val runtime = getDotNetRuntime(executable, project) ?: return

        withContext(Dispatchers.EDT) {
            createAndStartDebugSession(
                sessionId,
                Path(sessionModel.projectPath),
                executable,
                runtime,
                browserSettings,
                hostRunConfiguration,
                sessionProcessEventListener,
                sessionProcessTerminatedListener,
                sessionProcessLifetime,
                project
            )
        }
    }

    private suspend fun createAndStartDebugSession(
        sessionId: String,
        sessionProjectPath: Path,
        dotnetExecutable: DotNetExecutable,
        dotnetRuntime: DotNetCoreRuntime,
        browserSettings: StartBrowserSettings?,
        hostRunConfiguration: AspireHostConfiguration?,
        sessionProcessEventListener: ProcessListener,
        sessionProcessTerminatedListener: ProcessListener,
        sessionProcessLifetime: Lifetime,
        project: Project
    ) {
        val debuggerSessionId = ExecutionEnvironment.getNextUnusedExecutionId()

        val debuggerWorkerSession = initDebuggerSession(
            sessionId,
            debuggerSessionId,
            dotnetExecutable,
            dotnetRuntime,
            sessionProcessEventListener,
            sessionProcessTerminatedListener,
            sessionProcessLifetime,
            project,
            { }
        )

        val executionResult =
            executeDebuggerSession(debuggerWorkerSession, browserSettings, hostRunConfiguration, project)

        createAndStartSession(
            executionResult.executionConsole,
            null,
            project,
            sessionProcessLifetime,
            executionResult.processHandler,
            debuggerWorkerSession.protocol,
            debuggerWorkerSession.debugSessionModel,
            object : IDebuggerOutputListener {},
            debuggerSessionId
        ) { xDebuggerManager, xDebugProcessStarter ->
            xDebuggerManager.startSessionAndShowTab(
                sessionProjectPath.nameWithoutExtension,
                RiderIcons.RunConfigurations.DotNetProject,
                null,
                false,
                xDebugProcessStarter
            )
        }
    }

    private fun executeDebuggerSession(
        session: DebuggerWorkerSession,
        browserSettings: StartBrowserSettings?,
        hostRunConfiguration: AspireHostConfiguration?,
        project: Project
    ): ExecutionResult {
        val console = createConsole(
            ConsoleKind.Normal,
            session.debuggerWorkerProcessHandler.debuggerWorkerRealHandler,
            project
        )

        startBrowser(hostRunConfiguration, browserSettings, session.debuggerWorkerProcessHandler)

        return DefaultExecutionResult(console, session.debuggerWorkerProcessHandler)
    }
}