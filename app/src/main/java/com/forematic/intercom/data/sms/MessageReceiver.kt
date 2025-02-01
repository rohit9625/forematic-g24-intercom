package com.forematic.intercom.data.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import com.forematic.intercom.data.IntercomDataSource
import com.forematic.intercom.data.model.CallOutNumber
import com.forematic.intercom.data.model.IntercomCommand
import com.forematic.intercom.utils.CommandParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MessageReceiver(
    private val intercomDataSource: IntercomDataSource,
    private val smsManager: SmsManager
): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val extras = intent?.extras

        Log.d("IntercomMessageReceiver", "onReceive called")

        if(extras != null) {
            val pdus = extras.get("pdus") as Array<*>
            for (pdu in pdus) {
                val sms = SmsMessage.createFromPdu(pdu as ByteArray)
                val sender = sms.originatingAddress ?: ""
                val messageBody = sms.messageBody

                Log.d("IntercomMessageReceiver", "Sender: $sender, Message: $messageBody")

                val (command, extractedData) = CommandParser.parseCommand(messageBody)

                // Check for the programming password update pattern
                if (command != null) {
                    handleCommand(sender, command, extractedData)
                } else {
                    sendErrorResponse(sender, "Invalid Command Format")
                }
            }
        }
    }

    private fun handleCommand(phoneNumber: String, command: IntercomCommand, extractedData: String?) {
        Log.d("RohitVerma", "command: $command and extractedData: $extractedData")
        when(command) {
            IntercomCommand.PROGRAMMING_PASSWORD -> {
                extractedData?.let {
                    updatePasswordAndSendResponse(phoneNumber, it)
                }
            }
            IntercomCommand.REQUEST_SIGNAL_STRENGTH -> {
                sendSignalStrengthResponse(phoneNumber)
            }

            IntercomCommand.ADD_ADMIN_NUMBER -> {
                extractedData?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        intercomDataSource.changeAdminNumber(it)
                        smsManager.sendTextMessage(phoneNumber, null, "Admin number changed successfully", null, null)
                    }
                }
            }

            IntercomCommand.SET_CALLOUT_1 -> {
                extractedData?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        intercomDataSource.setCallOutNumber(it, CallOutNumber.FIRST)
                        smsManager.sendTextMessage(phoneNumber, null, "SUCCESS", null, null)
                    }
                }
            }
            IntercomCommand.SET_CALLOUT_2 -> {
                extractedData?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        intercomDataSource.setCallOutNumber(it, CallOutNumber.SECOND)
                        smsManager.sendTextMessage(phoneNumber, null, "SUCCESS", null, null)
                    }
                }
            }
            IntercomCommand.SET_CALLOUT_3 -> {
                extractedData?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        intercomDataSource.setCallOutNumber(it, CallOutNumber.THIRD)
                        smsManager.sendTextMessage(phoneNumber, null, "SUCCESS", null, null)
                    }
                }
            }
        }
    }

    private fun updatePasswordAndSendResponse(phoneNumber: String, newPassword: String) {
        CoroutineScope(Dispatchers.IO).launch {
            intercomDataSource.changeProgrammingPassword(newPassword)
            smsManager.sendTextMessage(phoneNumber, null, "Password updated successfully", null, null)
        }
    }

    private fun sendSignalStrengthResponse(phoneNumber: String) {
        CoroutineScope(Dispatchers.IO).launch {
            intercomDataSource.getIntercomDevice().collectLatest {
                smsManager.sendTextMessage(phoneNumber, null, "RSSI is ${it.signalStrength}", null, null)
            }
        }
    }

    private fun sendErrorResponse(phoneNumber: String, errorMessage: String) {
        smsManager.sendTextMessage(phoneNumber, null, errorMessage, null, null)
        Log.d("MessageReceiver", "Sent error response: $errorMessage")
    }
}