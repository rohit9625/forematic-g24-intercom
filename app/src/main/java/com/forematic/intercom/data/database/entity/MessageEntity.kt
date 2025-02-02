package com.forematic.intercom.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val content: String,
    val isSentByIntercom: Boolean,
    val timestamp: Long,
    val senderAddress: String?
)
