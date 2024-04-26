package me.rafaelldi.aspire.run

import com.intellij.execution.RunManager
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.openapi.project.Project
import com.jetbrains.rider.model.RunnableProject
import com.jetbrains.rider.run.AutoGeneratedRunConfigurationManager
import com.jetbrains.rider.run.ExternalRunConfigurationGeneratorExtension

class AspireHostRunConfigurationGenerator(private val project: Project) : ExternalRunConfigurationGeneratorExtension {
    override fun generateConfigurations(
        runnableProjects: List<RunnableProject>,
        autoGeneratedRunConfigurationManager: AutoGeneratedRunConfigurationManager,
        runManager: RunManager
    ): List<Pair<RunnableProject, RunnerAndConfigurationSettings>> {
        val applicableProjects = runnableProjects.filter {
            it.kind == AspireRunnableProjectKinds.AspireHost &&
                    !autoGeneratedRunConfigurationManager.hasRunConfigurationEverBeenGenerated(
                        it.projectFilePath,
                        it.kind
                    )
        }

        return emptyList()
    }
}