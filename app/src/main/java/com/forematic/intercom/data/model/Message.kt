package com.forematic.intercom.data.model

import com.forematic.intercom.data.database.entity.MessageEntity

data class Message(
    val id: Int = 0,
    val content: String,
    val isSentByIntercom: Boolean,
    val senderAddress: String? = null
)

fun Message.toEntity() = MessageEntity(
    content = content,
    isSentByIntercom = isSentByIntercom,
    timestamp = System.currentTimeMillis(),
    senderAddress = senderAddress
)

fun MessageEntity.toMessage() = Message(
    id = id,
    content = content,
    isSentByIntercom = isSentByIntercom,
    senderAddress = senderAddress
)
