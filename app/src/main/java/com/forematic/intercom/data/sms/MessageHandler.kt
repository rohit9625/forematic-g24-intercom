package com.forematic.intercom.data.sms

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.forematic.intercom.data.MessageDataSource
import com.forematic.intercom.data.model.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessageHandler(
    private val context: Context,
    private val smsManager: SmsManager,
    private val messageDataSource: MessageDataSource
) {
    private var sentStatusReceiver: BroadcastReceiver? = null
    private var deliveredStatusReceiver: BroadcastReceiver? = null

    fun sendTextMessage(recipient: String, text: String) {
        val sentPendingIntent = PendingIntent.getBroadcast(
            context, 0,
            Intent(ACTION_SMS_SENT).apply {
                putExtra(EXTRA_MESSAGE_CONTENT, text)
                putExtra(EXTRA_MESSAGE_RECIPIENT, recipient)
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val deliveredPendingIntent = PendingIntent.getBroadcast(
            context, 0,
            Intent(ACTION_SMS_DELIVERED),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        try {
            smsManager.sendTextMessage(recipient, null, text, sentPendingIntent, deliveredPendingIntent)
        } catch (e: Exception) {
            Log.e("MessageHandler", "Error while sending SMS", e)
        }
    }

    private fun insertMessage(recipient: String, message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            messageDataSource.insertMessage(
                Message(content = message, isSentByIntercom = true, senderAddress = recipient)
            )
        }
    }

    fun registerBroadcastReceivers(context: Context) {
        sentStatusReceiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when(resultCode) {
                    Activity.RESULT_OK -> {
                        Log.d("MessageHandler", "SMS sent successfully")
                        val messageContent = intent?.getStringExtra(EXTRA_MESSAGE_CONTENT)
                        val recipient = intent?.getStringExtra(EXTRA_MESSAGE_RECIPIENT)
                        if(messageContent != null && recipient != null)
                            insertMessage(recipient, messageContent)
                    }
                    else -> {
                        Log.e("MessageHandler", "Failed to send SMS with result code: $resultCode")
                        Toast.makeText(context, "Failed to send SMS", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        deliveredStatusReceiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when(resultCode) {
                    Activity.RESULT_OK -> {
                        Log.d("MessageHandler", "SMS delivered successfully")
                        Toast.makeText(context, "SMS delivered successfully", Toast.LENGTH_SHORT).show()
                    }
                    Activity.RESULT_CANCELED -> {
                        Log.d("MessageHandler", "Failed to deliver SMS")
                    }
                }
            }
        }

        ContextCompat.registerReceiver(
            context,
            sentStatusReceiver,
            IntentFilter(ACTION_SMS_SENT),
            ContextCompat.RECEIVER_EXPORTED
        )

        ContextCompat.registerReceiver(
            context,
            sentStatusReceiver,
            IntentFilter(ACTION_SMS_DELIVERED),
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    fun unregisterReceivers() {
        sentStatusReceiver?.let {
            context.unregisterReceiver(it)
            sentStatusReceiver = null
        }
        deliveredStatusReceiver?.let {
            context.unregisterReceiver(it)
            deliveredStatusReceiver = null
        }
    }

    companion object {
        private const val ACTION_SMS_SENT = "com.forematic.intercom.ACTION_SMS_SENT"
        private const val ACTION_SMS_DELIVERED = "com.forematic.intercom.ACTION_SMS_DELIVERED"
        private const val EXTRA_MESSAGE_CONTENT = "extra_message_content"
        private const val EXTRA_MESSAGE_RECIPIENT = "extra_message_recipient"
    }
}