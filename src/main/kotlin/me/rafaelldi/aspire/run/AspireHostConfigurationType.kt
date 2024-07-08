package me.rafaelldi.aspire.run

import com.intellij.execution.RunManager
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rider.model.RunnableProject
import com.jetbrains.rider.model.RunnableProjectKind
import com.jetbrains.rider.run.AutoGeneratedRunConfigurationManager
import com.jetbrains.rider.run.configurations.IRunConfigurationWithDefault
import com.jetbrains.rider.run.configurations.IRunnableProjectConfigurationType
import com.jetbrains.rider.run.configurations.RunConfigurationHelper.hasConfigurationForNameAndTypeId
import com.jetbrains.rider.run.configurations.launchSettings.LaunchSettingsJsonService
import me.rafaelldi.aspire.AspireIcons

class AspireHostConfigurationType : ConfigurationTypeBase(
    ID,
    "Aspire Host",
    "Aspire Host configuration",
    AspireIcons.RunConfig
), IRunnableProjectConfigurationType, IRunConfigurationWithDefault {
    companion object {
        const val ID = "AspireHostConfiguration"
    }

    private val factory = AspireHostConfigurationFactory(this)

    init {
        addFactory(factory)
    }

    override fun isApplicable(kind: RunnableProjectKind) = kind == AspireRunnableProjectKinds.AspireHost

    override suspend fun tryCreateDefault(
        project: Project,
        lifetime: Lifetime,
        projects: List<RunnableProject>,
        autoGeneratedRunConfigurationManager: AutoGeneratedRunConfigurationManager,
        runManager: RunManager
    ): List<Pair<RunnableProject, RunnerAndConfigurationSettings>> {
        val aspireHostProjects = projects.filter { it.kind == AspireRunnableProjectKinds.AspireHost }

        val result = mutableListOf<Pair<RunnableProject, RunnerAndConfigurationSettings>>()

        aspireHostProjects.forEach { runnableProject ->
            LaunchSettingsJsonService.loadLaunchSettings(runnableProject)?.profiles?.forEach { profile ->
                if (!profile.value.commandName.equals("Project", true))
                    return@forEach

                if (hasRunConfigurationEverBeenGenerated(
                        autoGeneratedRunConfigurationManager,
                        runnableProject.projectFilePath,
                        profile.key
                    )
                ) return@forEach

                val configurationName =
                    if (runnableProject.name == profile.key) profile.key
                    else "${runnableProject.name}: ${profile.key}"

                if (runManager.hasConfigurationForNameAndTypeId(configurationName, ID) ||
                    runManager.hasConfigurationForNameAndTypeId(runnableProject.name, ID))
                    return@forEach

                val configuration = generateConfigurationForProfile(
                    configurationName,
                    runnableProject,
                    profile.key,
                    runManager
                )

                runManager.addConfiguration(configuration)
                markProjectAsAutoGenerated(
                    autoGeneratedRunConfigurationManager,
                    runnableProject.projectFilePath,
                    profile.key
                )
                result.add(runnableProject to configuration)
            }
        }

        return result
    }

    private fun hasRunConfigurationEverBeenGenerated(
        autoGeneratedRunConfigurationManager: AutoGeneratedRunConfigurationManager,
        projectFilePath: String,
        profileName: String
    ) = autoGeneratedRunConfigurationManager.hasRunConfigurationEverBeenGenerated(
        projectFilePath,
        mapOf(
            "aspireProfileName" to profileName,
        )
    )

    private fun markProjectAsAutoGenerated(
        autoGeneratedRunConfigurationManager: AutoGeneratedRunConfigurationManager,
        projectFilePath: String,
        profileName: String
    ) {
        autoGeneratedRunConfigurationManager.markProjectAsAutoGenerated(
            projectFilePath,
            mapOf(
                "aspireProfileName" to profileName,
            )
        )
    }

    private fun generateConfigurationForProfile(
        name: String,
        runnableProject: RunnableProject,
        profile: String,
        runManager: RunManager
    ): RunnerAndConfigurationSettings {
        val settings = runManager.createConfiguration(name, factory).apply {
            isActivateToolWindowBeforeRun = false
            isFocusToolWindowBeforeRun = false
        }
        (settings.configuration as? AspireHostConfiguration)?.parameters?.apply {
            projectFilePath = runnableProject.projectFilePath
            profileName = profile
            val launchProfile = getLaunchProfileByName(runnableProject, profile)
            if (launchProfile != null) {
                val environmentVariables = getEnvironmentVariables(launchProfile.first, launchProfile.second)
                envs = environmentVariables
                trackEnvs = true
                val applicationUrl = getApplicationUrl(launchProfile.second)
                startBrowserParameters.apply {
                    url = applicationUrl ?: ""
                    startAfterLaunch = launchProfile.second.launchBrowser
                }
                trackUrl = true
            }
        }

        return settings
    }

    override fun getHelpTopic() = "me.rafaelldi.aspire.run-config"
}