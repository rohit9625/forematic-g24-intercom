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
    SET_TIMEZONE_MODE("\\d{4}#(FREE|NIGHT|DAY|HOLS)#"),

    // CLI Commands
    SET_CLI_MODE("\\d{4}#(ANY|AUTH)#"),
    SET_CLI_NUMBER("\\d{4}#11#201#(\\d{8,11})#"),
    FIND_NEXT_CLI_LOCATION("\\d{4}#CIA\\?#"),

    // Delivery Code Commands (Single Use Code)
    FIND_DELIVERY_CODE_LOCATION("\\d{4}#SUA\\?#"),

    // Relay Commands
    SET_RELAY1_OUTPUT_NAME("\\d{4}#ID1#([A-Za-z ]+?)#"),
    SET_RELAY2_OUTPUT_NAME("\\d{4}#ID2#([A-Za-z ]+?)#"),
    SET_RELAY1_TIME("\\d{4}#RL1T#(\\d{2})#"),
    SET_RELAY2_TIME("\\d{4}#RL2T#(\\d{2})#"),
    FIND_NEXT_RELAY1_LOCATION("\\d{4}#R1A\\?#"),
    FIND_NEXT_RELAY2_LOCATION("\\d{4}#R2A\\?#"),
    SET_RELAY_KEYPAD_CODE("\\d{4}#11#(\\d{3})#(\\d{2,8})#");

    companion object  {
        fun fromMessage(message: String): IntercomCommand? {
            return entries.firstOrNull { message.matches(it.pattern.toRegex()) }
        }
    }
}