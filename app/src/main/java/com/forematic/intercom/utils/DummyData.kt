package com.forematic.intercom.utils

import com.forematic.intercom.Message

object DummyData {
    val messages = listOf(
        Message("I'm fine, thanks!", false),
        Message("How are you?", true),
        Message("Hi", false),
        Message("Hello", true),
    )
}