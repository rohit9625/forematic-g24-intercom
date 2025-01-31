package com.forematic.intercom.ui.states

import com.forematic.intercom.Message

data class MainUiState(
    val messages: List<Message> = emptyList(),
    val programmingPassword: String  = "",
    val isIntercomOn: Boolean = true
)
