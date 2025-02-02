package com.forematic.intercom.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.forematic.intercom.data.database.dao.IntercomDao
import com.forematic.intercom.data.database.dao.RelayDao
import com.forematic.intercom.data.database.entity.IntercomEntity
import com.forematic.intercom.data.database.entity.RelayEntity

@Database(
    entities = [IntercomEntity::class, RelayEntity::class],
    version = 5
)
abstract class IntercomDatabase: RoomDatabase() {
    abstract val intercomDao: IntercomDao
    abstract val relayDao: RelayDao

    companion object {
        @Volatile
        private var INSTANCE: IntercomDatabase? = null

        fun getDatabase(context: Context): IntercomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IntercomDatabase::class.java,
                    "intercom_database"
                ).fallbackToDestructiveMigration().build().also {
                    INSTANCE = it
                }
                instance
            }
        }
    }
}