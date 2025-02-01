package com.forematic.intercom.di

import android.telephony.SmsManager
import com.forematic.intercom.data.IntercomDataSource
import com.forematic.intercom.data.MessageDataSource
import com.forematic.intercom.data.database.IntercomDatabase
import com.forematic.intercom.data.database.MessageDatabase
import com.forematic.intercom.data.database.dao.IntercomDao
import com.forematic.intercom.data.database.dao.MessageDao

interface DatabaseModule {
    val intercomDatabase: IntercomDatabase

    val messageDatabase: MessageDatabase

    val intercomDao: IntercomDao

    val messageDao: MessageDao

    val smsManager: SmsManager

    val intercomDataSource: IntercomDataSource

    val messageDataSource: MessageDataSource
}