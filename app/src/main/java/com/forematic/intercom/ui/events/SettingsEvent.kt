package com.forematic.intercom.ui.events

sealed interface SettingsEvent {
    data class OnProgrammingPasswordChanged(val value: String): SettingsEvent
    data class OnSignalStrengthChanged(val value: String): SettingsEvent
    data object OnSaveClicked: SettingsEvent
}