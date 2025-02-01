package com.forematic.intercom.utils

import com.forematic.intercom.data.model.Message

object DummyData {
    val messages = listOf(
        Message(content = "I'm fine, thanks!", isSentByIntercom =  false),
        Message(content = "How are you?", isSentByIntercom =  true),
        Message(content = "Hi", isSentByIntercom =  false),
        Message(content = "Hello", isSentByIntercom =  true),
    )
}