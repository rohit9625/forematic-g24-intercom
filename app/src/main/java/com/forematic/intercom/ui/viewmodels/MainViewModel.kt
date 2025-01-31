package com.forematic.intercom.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forematic.intercom.data.IntercomDataSource
import com.forematic.intercom.ui.states.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val intercomDataSource: IntercomDataSource
): ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            intercomDataSource.getIntercomDevice().collect { intercom ->
                _uiState.value = _uiState.value.copy(
                    programmingPassword = intercom.programmingPassword
                )
            }
        }
    }
}