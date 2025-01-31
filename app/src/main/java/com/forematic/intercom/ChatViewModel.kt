package com.forematic.intercom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Message(val content: String, val isSentByUser: Boolean)

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    var newMessage by mutableStateOf("")
        private set

    fun onMessageChange(text: String) {
        newMessage = text
    }

    fun sendMessage() {
        if (newMessage.isNotBlank()) {
            _messages.value = listOf(Message(newMessage, true)) + _messages.value
            newMessage = ""
        }
    }
}