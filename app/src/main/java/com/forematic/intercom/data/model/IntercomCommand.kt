package com.forematic.intercom.data.model

enum class IntercomCommand(val pattern: String) {
    PROGRAMMING_PASSWORD("\\d{4}#PWD#(\\d{4})#"),
    REQUEST_SIGNAL_STRENGTH("(\\d{4})#RSSI\\?#"),
    ADD_ADMIN_NUMBER("\\d{4}#00#(\\d{10,15})#");

    companion object  {
        fun fromMessage(message: String): IntercomCommand? {
            return entries.firstOrNull { message.matches(it.pattern.toRegex()) }
        }
    }
}