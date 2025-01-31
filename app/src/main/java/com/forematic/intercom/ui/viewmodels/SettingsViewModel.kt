package com.forematic.intercom.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forematic.intercom.data.IntercomDataSource
import com.forematic.intercom.ui.events.SettingsEvent
import com.forematic.intercom.ui.states.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val intercomDataSource: IntercomDataSource
): ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            intercomDataSource.getIntercomDevice().collect { intercom ->
                _uiState.update { it.copy(
                    isLoading = false,
                    programmingPassword = intercom.programmingPassword,
                    signalStrength = intercom.signalStrength.toString(),
                ) }
            }
        }
    }

    fun onEvent(e: SettingsEvent) {
        when(e) {
            is SettingsEvent.OnProgrammingPasswordChanged -> {
                _uiState.update { it.copy(programmingPassword = e.value) }
            }
            is SettingsEvent.OnSignalStrengthChanged -> {
                _uiState.update { it.copy(signalStrength = e.value) }
            }

            SettingsEvent.OnSaveClicked -> {
                /*TODO("Update changes to the local database")*/
            }
        }
    }
}