package com.forematic.intercom

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.forematic.intercom.data.IntercomDataSource
import com.forematic.intercom.data.sms.MessageReceiver
import com.forematic.intercom.ui.MainScreen
import com.forematic.intercom.ui.SettingScreen
import com.forematic.intercom.ui.theme.G24IntercomTheme
import com.forematic.intercom.ui.viewmodels.MainViewModel
import com.forematic.intercom.ui.viewmodels.SettingsViewModel
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    private lateinit var messageReceiver: MessageReceiver
    private lateinit var intercomDataSource: IntercomDataSource
    private var shouldShowPermissionRationale by mutableStateOf(false)

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        shouldShowPermissionRationale = permissions[Manifest.permission.SEND_SMS] != true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intercomDataSource = IntercomDataSource(IntercomApplication.databaseModule.intercomDao)
        messageReceiver = MessageReceiver(intercomDataSource, IntercomApplication.databaseModule.smsManager)

        val intentFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(messageReceiver, intentFilter)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            G24IntercomTheme {
                NavHost(navController = navController, startDestination = Route.Main) {
                    composable<Route.Main> {
                        val mainViewModel = viewModel<MainViewModel> {
                            MainViewModel(intercomDataSource)
                        }
                        val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()

                        MainScreen(
                            uiState = uiState,
                            onNavigateToSettings = { navController.navigate(Route.Settings) },
                            onRequestPermission = ::requestSmsPermissions,
                            showPermissionRationale = shouldShowPermissionRationale,
                            onEvent = mainViewModel::onEvent
//                            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)
                        )
                    }

                    composable<Route.Settings> {
                        val settingsViewmodel = viewModel<SettingsViewModel> {
                            SettingsViewModel(intercomDataSource)
                        }
                        val uiState by settingsViewmodel.uiState.collectAsStateWithLifecycle()

                        SettingScreen(
                            uiState = uiState,
                            onEvent = settingsViewmodel::onEvent,
                            onNavigateBack = { navController.navigateUp() }
                        )
                    }
                }
            }
        }
    }

    private fun hasSmsPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this, Manifest.permission.RECEIVE_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestSmsPermissions() {
        if (!hasSmsPermissions()) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()

        shouldShowPermissionRationale = !hasSmsPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(messageReceiver)
    }
}

sealed interface Route {
    @Serializable
    data object Main: Route

    @Serializable
    data object Settings: Route
}

