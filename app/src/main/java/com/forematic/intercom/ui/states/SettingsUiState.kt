package com.forematic.intercom.ui.states

data class SettingsUiState(
    val isLoading: Boolean = false,
    val programmingPassword: String = "",
    val signalStrength: String = "4",
    val signalStrengthResponse: String = ""
)