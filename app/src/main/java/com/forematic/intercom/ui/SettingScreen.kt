package com.forematic.intercom.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.forematic.intercom.ui.components.EditableTextField
import com.forematic.intercom.ui.events.SettingsEvent
import com.forematic.intercom.ui.states.SettingsUiState
import com.forematic.intercom.ui.theme.G24IntercomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    uiState: SettingsUiState,
    onEvent: (SettingsEvent)-> Unit,
    onNavigateBack: ()-> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            "Navigate Back"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = { }
                    ) {
                        Text(text = "Save")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(innerPadding)
        ) {
            if(uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxSize()
                ) {
                    EditableTextField(
                        value = uiState.programmingPassword,
                        onValueChange = {
                            onEvent(SettingsEvent.OnProgrammingPasswordChanged(it))
                        },
                        label = "Programming Password",
                        modifier = Modifier.padding(16.dp)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        EditableTextField(
                            value = uiState.signalStrength,
                            onValueChange = {
                                onEvent(SettingsEvent.OnSignalStrengthChanged(it))
                            },
                            label = "Signal Strength",
                            modifier = Modifier.weight(0.4f)
                        )
                        EditableTextField(
                            value = uiState.signalStrengthResponse,
                            onValueChange = { },
                            label = "Response",
                            modifier = Modifier.weight(0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SettingScreenPreview() {
    G24IntercomTheme {
        SettingScreen(
            uiState = SettingsUiState(programmingPassword = "1234", signalStrengthResponse = "RSSI is 4"),
            onEvent = { },
            onNavigateBack = { }
        )
    }
}