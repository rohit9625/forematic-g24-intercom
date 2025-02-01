package com.forematic.intercom.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.forematic.intercom.data.model.Message
import com.forematic.intercom.R
import com.forematic.intercom.ui.components.MessageBubble
import com.forematic.intercom.ui.components.SwitchWithText
import com.forematic.intercom.ui.events.MainScreenEvent
import com.forematic.intercom.ui.states.MainUiState
import com.forematic.intercom.ui.theme.G24IntercomTheme
import com.forematic.intercom.utils.DummyData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    uiState: MainUiState,
    onEvent: (MainScreenEvent) -> Unit,
    onNavigateToSettings: ()-> Unit,
    onRequestPermission: ()-> Unit,
    showPermissionRationale: Boolean = false,
    isPermanentlyDeclined: Boolean = false
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isQuickSetupDialogOpen by remember { mutableStateOf(false) }

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
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            uiState.programmingPassword?.let {
                Text(
                    text = "Programming Password: $it",
                    style = MaterialTheme.typography.titleSmall
                )
            }

            if(showPermissionRationale) {
                PermissionRational(
                    onAction = onRequestPermission,
                    isPermanentlyDeclined = isPermanentlyDeclined,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxSize()
                )
            } else {
                if(uiState.programmingPassword != null) {
                    ChatScreen(
                        messages = uiState.messages,
                        newMessage = Message(
                            content = "",
                            isSentByIntercom = true
                        )
                    )
                }
                Button(
                    onClick = { isQuickSetupDialogOpen = true }
                ) {
                    Text(
                        text = "Quick Setup"
                    )
                }
            }
        }

        if(isQuickSetupDialogOpen) {
            QuickSetupDialog(
                onSubmit = { phoneNumber, callOutNumber ->
                    onEvent(MainScreenEvent.OnSetupIntercom(phoneNumber, callOutNumber))
                },
                onDismiss = { isQuickSetupDialogOpen = false }
            )
        }
    }
}

@Composable
fun QuickSetupDialog(
    onSubmit: (adminNumber: String, callOutNumber: String) -> Unit,
    onDismiss: ()-> Unit
) {
    var adminNumber by remember { mutableStateOf("") }
    var callOutNumber by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = MaterialTheme.shapes.extraLarge) {
            Column(
                modifier = Modifier.padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Quick Keypad Setup",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.widthIn(max = 228.dp)
                ) {
                    OutlinedTextField(
                        value = adminNumber,
                        onValueChange = { adminNumber = it },
                        label = { Text(text = "Admin Number") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        ),
                        shape = MaterialTheme.shapes.medium
                    )

                    OutlinedTextField(
                        value = callOutNumber,
                        onValueChange = { callOutNumber = it },
                        label = { Text(text = "First Call-Out Number") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Done
                        ),
                        shape = MaterialTheme.shapes.medium
                    )
                }

                Button(
                    onClick = { onSubmit(adminNumber, callOutNumber); onDismiss() }
                ) {
                    Text(text = "Done")
                }
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
            onEvent = { },
            showPermissionRationale = false,
            onNavigateToSettings = { },
            onRequestPermission = { }
        )
    }
}