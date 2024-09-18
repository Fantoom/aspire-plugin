package com.jetbrains.rider.aspire.settings

import com.intellij.openapi.components.*

@State(
    name = "com.jetbrains.rider.aspire.settings.AspireSettings",
    storages = [(Storage("AspireSettings.xml"))]
)
@Service
class AspireSettings : SimplePersistentStateComponent<AspireSettingsState>(AspireSettingsState()) {
    companion object {
        fun getInstance() = service<AspireSettings>()
    }

    var connectToDatabase
        get() = state.connectToDatabase
        set(value) {
            state.connectToDatabase = value
        }
}