package com.forematic.intercom.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forematic.intercom.data.IntercomDataSource
import com.forematic.intercom.data.MessageDataSource
import com.forematic.intercom.data.model.IntercomDevice
import com.forematic.intercom.data.model.Relay
import com.forematic.intercom.ui.events.MainScreenEvent
import com.forematic.intercom.ui.states.MainUiState
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val intercomDataSource: IntercomDataSource,
    private val messageDataSource: MessageDataSource
): ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            intercomDataSource.getIntercomDevice()
                .combine(messageDataSource.getAllMessages()) { intercom, messages ->
                    Pair(intercom, messages)
                }.collect { (intercom, messages) ->
                    _uiState.update {
                        it.copy(
                            programmingPassword = intercom.programmingPassword,
                            adminNumber = intercom.adminNumber,
                            messages = messages
                        )
                    }
            }
        }
    }

    fun onEvent(e: MainScreenEvent) {
        when(e) {
            is MainScreenEvent.OnSetupIntercom -> {
                viewModelScope.launch {
                    val firstRelayId = intercomDataSource.setupRelay(relay = Relay(keypadCodeLocation = "038"))
                    val secondRelayId = intercomDataSource.setupRelay(relay = Relay(keypadCodeLocation = "138"))

                    intercomDataSource.setupIntercom(
                        IntercomDevice(
                            programmingPassword = "1234",
                            adminNumber = e.phoneNumber,
                            signalStrength = 4,
                            firstCallOutNumber = e.callOutNumber,
                            firstRelay = Relay(id = firstRelayId, keypadCodeLocation = "038"),
                            secondRelay = Relay(id = secondRelayId, keypadCodeLocation = "138")
                        )
                    )
                }
            }
        }
    }
}