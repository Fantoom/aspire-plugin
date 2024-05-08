package me.rafaelldi.aspire.actions.dashboard

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import me.rafaelldi.aspire.generated.ResourceState
import me.rafaelldi.aspire.generated.ResourceType
import me.rafaelldi.aspire.sessionHost.SessionManager
import me.rafaelldi.aspire.util.ASPIRE_RESOURCE_STATE
import me.rafaelldi.aspire.util.ASPIRE_RESOURCE_TYPE
import me.rafaelldi.aspire.util.ASPIRE_RESOURCE_UID

class StartResourceAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val resourceUid = event.getData(ASPIRE_RESOURCE_UID) ?: return
        val resourceType = event.getData(ASPIRE_RESOURCE_TYPE) ?: return
        val resourceState = event.getData(ASPIRE_RESOURCE_STATE) ?: return

        if (resourceType != ResourceType.Project || resourceState != ResourceState.Finished) {
            return
        }

        SessionManager.getInstance(project).startResource(resourceUid)
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        if (project == null) {
            event.presentation.isEnabledAndVisible = false
            return
        }

        val resourceUid = event.getData(ASPIRE_RESOURCE_UID)
        val resourceType = event.getData(ASPIRE_RESOURCE_TYPE)
        val resourceState = event.getData(ASPIRE_RESOURCE_STATE)
        if (resourceUid == null || resourceType == null || resourceState == null) {
            event.presentation.isEnabledAndVisible = false
            return
        }

        if (resourceType != ResourceType.Project || resourceState != ResourceState.Finished) {
            event.presentation.isEnabledAndVisible = false
            return
        }

        if (!SessionManager.getInstance(project).isResourceStopped(resourceUid)) {
            event.presentation.isEnabledAndVisible = false
            return
        }

        event.presentation.isEnabledAndVisible = true
    }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}