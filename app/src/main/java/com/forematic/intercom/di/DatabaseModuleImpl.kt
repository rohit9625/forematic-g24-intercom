package com.forematic.intercom.di

import android.content.Context
import com.forematic.intercom.data.database.IntercomDatabase
import com.forematic.intercom.data.database.dao.IntercomDao

class DatabaseModuleImpl(private val context: Context): DatabaseModule {
    override val intercomDatabase: IntercomDatabase by lazy {
        IntercomDatabase.getDatabase(context)
    }

    override val intercomDao: IntercomDao by lazy {
        intercomDatabase.intercomDao
    }
}