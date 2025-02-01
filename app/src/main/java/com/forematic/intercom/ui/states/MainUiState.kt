package com.forematic.intercom.ui.states

import com.forematic.intercom.data.model.Message

data class MainUiState(
    val messages: List<Message> = emptyList(),
    val programmingPassword: String?  = null,
    val adminNumber: String? = null,
    val isIntercomOn: Boolean = true
)
