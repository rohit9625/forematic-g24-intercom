package com.forematic.intercom.data

import com.forematic.intercom.data.database.dao.MessageDao
import com.forematic.intercom.data.model.Message
import com.forematic.intercom.data.model.toEntity
import com.forematic.intercom.data.model.toMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessageDataSource(
    private val messageDao: MessageDao
) {
    suspend fun insertMessage(message: Message) {
        messageDao.insertMessage(message.toEntity())
    }

    fun getAllMessages(): Flow<List<Message>> {
        return messageDao.getAllMessages().map { messages -> messages.map { it.toMessage() } }
    }
}