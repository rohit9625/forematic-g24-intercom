package com.forematic.intercom.ui.events

sealed interface MainScreenEvent {
    data class OnSetupIntercom(val phoneNumber: String, val callOutNumber: String): MainScreenEvent
}