package com.forematic.intercom.di

import android.telephony.SmsManager
import com.forematic.intercom.data.database.IntercomDatabase
import com.forematic.intercom.data.database.dao.IntercomDao

interface DatabaseModule {
    val intercomDatabase: IntercomDatabase

    val intercomDao: IntercomDao

    val smsManager: SmsManager
}