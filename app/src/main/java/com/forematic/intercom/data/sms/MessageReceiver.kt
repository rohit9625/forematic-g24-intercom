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

            IntercomCommand.SET_RELAY1_TIME -> {
                extractedData?.let {
                    updateRelayTime(phoneNumber, 1, it.toIntOrNull() ?: 5)
                }
            }
            IntercomCommand.SET_RELAY2_TIME -> {
                extractedData?.let {
                    updateRelayTime(phoneNumber, 2, it.toIntOrNull() ?: 5)
                }
            }

            IntercomCommand.FIND_NEXT_RELAY1_LOCATION -> sendRelayNextFreeLocation(relayId = 1, phoneNumber)
            IntercomCommand.FIND_NEXT_RELAY2_LOCATION -> sendRelayNextFreeLocation(relayId = 2, phoneNumber)
            IntercomCommand.FIND_NEXT_CLI_LOCATION -> sendCliNumberLocation(phoneNumber)
            IntercomCommand.FIND_DELIVERY_CODE_LOCATION -> sendDeliveryCodeLocation(phoneNumber)

            IntercomCommand.SET_CLI_NUMBER -> {
                extractedData?.let { cliNumber ->
                    setCliNumberAndSendResponse(phoneNumber, cliNumber)
                }
            }
        }
    }

    private fun setCliNumberAndSendResponse(recipient: String, number: String) {
        CoroutineScope(Dispatchers.IO).launch {
            intercomDataSource.setCliNumber(number)
            messageHandler.sendTextMessage(recipient, "CLI number updated successfully")
        }
    }

    private fun sendDeliveryCodeLocation(phoneNumber: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val deliveryCodeLocation = intercomDataSource.getIntercomDevice().first().deliveryCodeLocation
            messageHandler.sendTextMessage(phoneNumber, "Location is $deliveryCodeLocation")
        }
    }

    private fun sendCliNumberLocation(phoneNumber: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val cliLocation = intercomDataSource.getIntercomDevice().first().cliLocation
            messageHandler.sendTextMessage(phoneNumber, "Location is $cliLocation")
        }
    }

    private fun sendRelayNextFreeLocation(relayId: Long, phoneNumber: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val location = intercomDataSource.getRelayById(relayId).keypadCodeLocation
            messageHandler.sendTextMessage(phoneNumber, "Next free location is $location")
        }
    }

    private fun updateRelayTime(phoneNumber: String, relayId: Long, time: Int) {
        if(time in 1..99) {
            CoroutineScope(Dispatchers.IO).launch {
                intercomDataSource.setRelayTime(relayId, time)
                messageHandler.sendTextMessage(phoneNumber, "Relay time updated successfully")
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