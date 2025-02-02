package com.forematic.intercom.data.model

enum class IntercomCommand(val pattern: String) {
    PROGRAMMING_PASSWORD("\\d{4}#PWD#(\\d{4})#"),
    REQUEST_SIGNAL_STRENGTH("(\\d{4})#RSSI\\?#"),
    ADD_ADMIN_NUMBER("\\d{4}#00#(\\d{10,15})#"),
    SET_CALLOUT_1("\\d{4}#01#(\\d{10,15})#"),
    SET_CALLOUT_2("\\d{4}#02#(\\d{10,15})#"),
    SET_CALLOUT_3("\\d{4}#03#(\\d{10,15})#"),
    SET_MIC_VOLUME("\\d{4}#MIC#(\\d{2})#"),
    SET_SPEAKER_VOLUME("\\d{4}#SP#(\\d{2})#"),
    SET_TIMEZONE_MODE("\\d{4}#(FREE|NIGHT|DAY|HOLS)#");

    companion object  {
        fun fromMessage(message: String): IntercomCommand? {
            return entries.firstOrNull { message.matches(it.pattern.toRegex()) }
        }
    }
}