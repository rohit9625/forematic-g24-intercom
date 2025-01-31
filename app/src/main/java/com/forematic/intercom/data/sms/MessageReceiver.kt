package com.forematic.intercom.data.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import com.forematic.intercom.data.IntercomDataSource
import com.forematic.intercom.utils.MessageUtils
import com.forematic.intercom.utils.MessageUtils.extractPassword
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessageReceiver(
    private val intercomDataSource: IntercomDataSource
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

                // Check for the programming password update pattern
                if (MessageUtils.isValidProgrammingPassword(messageBody)) {
                    val newPassword = extractPassword(messageBody)
                    if (newPassword != null) {
                        CoroutineScope(Dispatchers.Main).launch {
                            intercomDataSource.changeProgrammingPassword(newPassword)
                        }
                    }
                }
            }
        }
    }
}