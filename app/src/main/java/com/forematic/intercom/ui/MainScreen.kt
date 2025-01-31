package com.forematic.intercom.ui

import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.forematic.intercom.Message
import com.forematic.intercom.R
import com.forematic.intercom.ui.components.MessageBubble
import com.forematic.intercom.ui.components.SwitchWithText
import com.forematic.intercom.ui.states.MainUiState
import com.forematic.intercom.ui.theme.G24IntercomTheme
import com.forematic.intercom.utils.DummyData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    uiState: MainUiState,
    onNavigateToSettings: ()-> Unit,
    onRequestPermission: ()-> Unit,
    showPermissionRationale: Boolean = false,
    isPermanentlyDeclined: Boolean = false
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    SwitchWithText(
                        isChecked = uiState.isIntercomOn,
                        onCheckedChange = {  },
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    IconButton(
                        onClick = {
                            if(showPermissionRationale) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Please grant SMS permissions first.")
                                }
                            } else {
                                onNavigateToSettings()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text(
                text = "Programming Password: ${uiState.programmingPassword}",
                style = MaterialTheme.typography.titleSmall
            )

            if(showPermissionRationale) {
                PermissionRational(
                    onAction = onRequestPermission,
                    isPermanentlyDeclined = isPermanentlyDeclined,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxSize()
                )
            } else {
                ChatScreen(
                    messages = uiState.messages,
                    newMessage = Message(content = "Hello", isSentByUser = true)
                )
            }
        }
    }
}

@Composable
fun PermissionRational(
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
    isPermanentlyDeclined: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = if(isPermanentlyDeclined)
                stringResource(R.string.permanently_declined_permission_rationale)
                else stringResource(R.string.permission_rationale),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Button(onClick = onAction) {
            Text(
                text = if(isPermanentlyDeclined) "Go to Settings" else "Grant Permission"
            )
        }
    }
}

@Composable
fun ChatScreen(messages: List<Message>, newMessage: Message) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            reverseLayout = true
        ) {
            items(messages) { message ->
                MessageBubble(message)
            }
        }

        // Input Field
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newMessage.content,
                onValueChange = {  },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                textStyle = TextStyle(fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {  }) {
                Text("Send")
            }
        }
    }
}

@Preview
@Composable
private fun ChatScreenPreview() {
    G24IntercomTheme {
        MainScreen(
            uiState = MainUiState(messages = DummyData.messages, programmingPassword = "1234"),
            showPermissionRationale = false,
            onNavigateToSettings = { },
            onRequestPermission = { }
        )
    }
}