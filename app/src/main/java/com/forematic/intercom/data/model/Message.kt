package com.forematic.intercom.data.model

import com.forematic.intercom.data.database.entity.MessageEntity

data class Message(
    val id: Int = 0,
    val content: String,
    val isSentByIntercom: Boolean
)

fun Message.toEntity() = MessageEntity(
    content = content,
    isSentByIntercom = isSentByIntercom,
    timestamp = System.currentTimeMillis()
)

fun MessageEntity.toMessage() = Message(
    id = id,
    content = content,
    isSentByIntercom = isSentByIntercom
)
