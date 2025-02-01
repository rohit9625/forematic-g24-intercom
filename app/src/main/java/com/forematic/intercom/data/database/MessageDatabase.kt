package com.forematic.intercom.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.forematic.intercom.data.database.dao.MessageDao
import com.forematic.intercom.data.database.entity.MessageEntity

@Database(
    entities = [MessageEntity::class],
    version = 1
)
abstract class MessageDatabase: RoomDatabase() {
    abstract val messageDao: MessageDao

    companion object {
        @Volatile
        private var INSTANCE: MessageDatabase? = null

        fun getDatabase(context: Context): MessageDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MessageDatabase::class.java,
                    "message_database"
                ).fallbackToDestructiveMigration().build().also {
                    INSTANCE = it
                }
                instance
            }
        }
    }
}