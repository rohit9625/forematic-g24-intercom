package com.forematic.intercom

import android.app.Application
import com.forematic.intercom.di.DatabaseModule
import com.forematic.intercom.di.DatabaseModuleImpl

class IntercomApplication: Application() {
    companion object {
        lateinit var databaseModule: DatabaseModule
    }
    override fun onCreate() {
        super.onCreate()
        databaseModule = DatabaseModuleImpl(this)
    }
}