package com.forematic.intercom.di

import android.content.Context
import android.os.Build
import android.telephony.SmsManager
import com.forematic.intercom.data.IntercomDataSource
import com.forematic.intercom.data.MessageDataSource
import com.forematic.intercom.data.database.IntercomDatabase
import com.forematic.intercom.data.database.MessageDatabase
import com.forematic.intercom.data.database.dao.IntercomDao
import com.forematic.intercom.data.database.dao.MessageDao
import com.forematic.intercom.data.database.dao.RelayDao
import com.forematic.intercom.data.sms.MessageHandler

class DatabaseModuleImpl(private val context: Context): DatabaseModule {
    override val intercomDatabase: IntercomDatabase by lazy {
        IntercomDatabase.getDatabase(context)
    }

    override val messageDatabase: MessageDatabase by lazy {
        MessageDatabase.getDatabase(context)
    }

    override val messageDao: MessageDao by lazy {
        messageDatabase.messageDao
    }

    override val relayDao: RelayDao by lazy {
        intercomDatabase.relayDao
    }

    override val intercomDataSource: IntercomDataSource by lazy {
        IntercomDataSource(intercomDao, relayDao)
    }

    override val messageDataSource: MessageDataSource by lazy {
        MessageDataSource(messageDao)
    }

    override val intercomDao: IntercomDao by lazy {
        intercomDatabase.intercomDao
    }

    override val smsManager: SmsManager by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(SmsManager::class.java)
        } else {
            SmsManager.getDefault()
        }
    }

    override val messageHandler: MessageHandler by lazy {
        MessageHandler(context, smsManager, messageDataSource)
    }
}