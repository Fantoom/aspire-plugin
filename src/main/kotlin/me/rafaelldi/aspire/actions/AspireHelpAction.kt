package me.rafaelldi.aspire.actions

import com.intellij.ide.actions.ContextHelpAction
import com.intellij.openapi.actionSystem.DataContext

class AspireHelpAction: ContextHelpAction() {
    override fun getHelpId(dataContext: DataContext?) = "me.rafaelldi.aspire.main"
}