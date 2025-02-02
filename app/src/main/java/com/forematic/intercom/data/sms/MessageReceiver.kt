package com.forematic.intercom.data.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import com.forematic.intercom.data.IntercomDataSource
import com.forematic.intercom.data.MessageDataSource
import com.forematic.intercom.data.model.CallOutNumber
import com.forematic.intercom.data.model.IntercomCommand
import com.forematic.intercom.data.model.Message
import com.forematic.intercom.utils.CommandParser
import com.forematic.intercom.utils.PhoneNumberUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MessageReceiver(
    private val intercomDataSource: IntercomDataSource,
    private val messageDataSource: MessageDataSource,
    private val messageHandler: MessageHandler
): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val extras = intent?.extras
        Log.d("IntercomMessageReceiver", "onReceive called")
        if(extras != null) {
            val pdus = extras.get("pdus") as Array<*>
            for (pdu in pdus) {
                val sms = SmsMessage.createFromPdu(pdu as ByteArray)
                val sender = sms.originatingAddress
                val messageBody = sms.messageBody

                sender?.let {
                    CoroutineScope(Dispatchers.Default).launch {
                        messageDataSource.insertMessage(
                            Message(content = messageBody, isSentByIntercom = false, senderAddress = sender)
                        )

                        if(isSenderAdmin(sender)) {
                            val (command, extractedData) = CommandParser.parseCommand(messageBody)

                            if (command != null) {
                                handleCommand(sender, command, extractedData)
                            } else {
                                messageHandler.sendTextMessage(sender, "Invalid message!")
                            }
                        } else {
                            messageHandler.sendTextMessage(sender, "Do not have permission!")
                        }
                    }
                }
            }
        }
    }

    private suspend fun isSenderAdmin(sender: String): Boolean {
        val adminNumber = intercomDataSource.getIntercomDevice().first().adminNumber

        return PhoneNumberUtils.arePhoneNumbersEqual(adminNumber, sender)
    }

    private fun handleCommand(phoneNumber: String, command: IntercomCommand, extractedData: String?) {
        when(command) {
            IntercomCommand.PROGRAMMING_PASSWORD -> {
                extractedData?.let {
                    updatePasswordAndSendResponse(phoneNumber, it)
                }
            }

            IntercomCommand.REQUEST_SIGNAL_STRENGTH -> sendSignalStrengthResponse(phoneNumber)

            IntercomCommand.ADD_ADMIN_NUMBER -> {
                extractedData?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        intercomDataSource.changeAdminNumber(it)
                        messageHandler.sendTextMessage(phoneNumber, "SUCCESS")
                    }
                }
            }

            IntercomCommand.SET_CALLOUT_1 -> {
                extractedData?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        intercomDataSource.setCallOutNumber(it, CallOutNumber.FIRST)
                        messageHandler.sendTextMessage(phoneNumber, "SUCCESS")
                    }
                }
            }
            IntercomCommand.SET_CALLOUT_2 -> {
                extractedData?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        intercomDataSource.setCallOutNumber(it, CallOutNumber.SECOND)
                        messageHandler.sendTextMessage(phoneNumber, "SUCCESS")
                    }
                }
            }
            IntercomCommand.SET_CALLOUT_3 -> {
                extractedData?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        intercomDataSource.setCallOutNumber(it, CallOutNumber.THIRD)
                        messageHandler.sendTextMessage(phoneNumber, "SUCCESS")
                    }
                }
            }

            IntercomCommand.SET_MIC_VOLUME -> {
                extractedData?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        intercomDataSource.setMicVolume(it.toIntOrNull() ?: 0)
                        messageHandler.sendTextMessage(phoneNumber, "SUCCESS")
                    }
                }
            }
            IntercomCommand.SET_SPEAKER_VOLUME -> {
                extractedData?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        intercomDataSource.setSpeakerVolume(it.toIntOrNull() ?: 0)
                        messageHandler.sendTextMessage(phoneNumber, "SUCCESS")
                    }
                }
            }

            IntercomCommand.SET_TIMEZONE_MODE -> {
                extractedData?.let { mode ->
                    CoroutineScope(Dispatchers.IO).launch {
                        intercomDataSource.setTimezoneMode(mode)
                        messageHandler.sendTextMessage(phoneNumber, "Mode changed to $mode")
                    }
                }
            }
        }
    }

    private fun updatePasswordAndSendResponse(phoneNumber: String, newPassword: String) {
        CoroutineScope(Dispatchers.IO).launch {
            intercomDataSource.changeProgrammingPassword(newPassword)
            messageHandler.sendTextMessage(phoneNumber, "Password updated successfully")
        }
    }

    private fun sendSignalStrengthResponse(phoneNumber: String) {
        CoroutineScope(Dispatchers.IO).launch {
            intercomDataSource.getIntercomDevice().collectLatest {
                messageHandler.sendTextMessage(phoneNumber, "RSSI is ${it.signalStrength}")
            }
        }
    }
}