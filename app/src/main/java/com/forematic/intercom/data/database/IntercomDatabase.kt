package com.forematic.intercom.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.forematic.intercom.data.database.dao.IntercomDao
import com.forematic.intercom.data.database.entity.IntercomEntity

@Database(
    entities = [IntercomEntity::class],
    version = 1
)
abstract class IntercomDatabase: RoomDatabase() {
    abstract val intercomDao: IntercomDao

    companion object {
        @Volatile
        private var INSTANCE: IntercomDatabase? = null

        fun getDatabase(context: Context): IntercomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IntercomDatabase::class.java,
                    "intercom_database"
                ).fallbackToDestructiveMigration().addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        db.execSQL("INSERT INTO intercom (programming_password, signal_strength) VALUES ('1234', 4)")
                    }
                }).build().also {
                    INSTANCE = it
                }
                instance
            }
        }
    }
}