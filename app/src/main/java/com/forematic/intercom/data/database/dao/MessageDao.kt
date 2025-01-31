package com.forematic.intercom.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.forematic.intercom.data.database.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert
    suspend fun insertMessage(message: MessageEntity)

    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    fun getAllMessages(): Flow<List<MessageEntity>>
}