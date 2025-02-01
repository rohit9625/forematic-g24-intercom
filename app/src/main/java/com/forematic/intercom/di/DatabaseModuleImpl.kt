package com.forematic.intercom.di

import android.content.Context
import android.os.Build
import android.telephony.SmsManager
import com.forematic.intercom.data.database.IntercomDatabase
import com.forematic.intercom.data.database.dao.IntercomDao

class DatabaseModuleImpl(private val context: Context): DatabaseModule {
    override val intercomDatabase: IntercomDatabase by lazy {
        IntercomDatabase.getDatabase(context)
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
}