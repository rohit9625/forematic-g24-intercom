package com.forematic.intercom.utils

import com.forematic.intercom.data.model.CommandData
import com.forematic.intercom.data.model.IntercomCommand

object CommandParser {
    fun parseCommand(message: String): Pair<IntercomCommand?, CommandData?> {
        val command = IntercomCommand.fromMessage(message)
        return if (command != null) {
            val extractedData = extractData(command, message)
            Pair(command, extractedData)
        } else {
            Pair(null, null) // Unknown command
        }
    }

    private fun extractData(command: IntercomCommand, message: String): CommandData? {
        return when (command) {
            IntercomCommand.PROGRAMMING_PASSWORD,
            IntercomCommand.ADD_ADMIN_NUMBER,
            IntercomCommand.SET_CALLOUT_1,
            IntercomCommand.SET_CALLOUT_2,
            IntercomCommand.SET_CALLOUT_3,
            IntercomCommand.SET_MIC_VOLUME,
            IntercomCommand.SET_SPEAKER_VOLUME,
            IntercomCommand.SET_TIMEZONE_MODE,
            IntercomCommand.SET_RELAY1_OUTPUT_NAME,
            IntercomCommand.SET_RELAY2_OUTPUT_NAME,
            IntercomCommand.SET_RELAY1_TIME,
            IntercomCommand.SET_RELAY2_TIME,
            IntercomCommand.SET_CLI_NUMBER,
            IntercomCommand.SET_CLI_MODE -> {
                val match = command.pattern.toRegex().find(message)
                val firstValue = match?.groupValues?.get(1)
                CommandData(firstValue)
            }

            IntercomCommand.SET_RELAY_KEYPAD_CODE -> {
                val match = command.pattern.toRegex().find(message)
                val location = match?.groupValues?.get(1) // Extract keypad code location
                val code = match?.groupValues?.getOrNull(2) // Extract keypad code
                CommandData(location, code)
            }

            else -> null // For now, other commands may not have additional data to extract
        }
    }
}