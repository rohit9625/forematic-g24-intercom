package com.forematic.intercom.utils

import com.forematic.intercom.data.model.IntercomCommand

object CommandParser {
    fun parseCommand(message: String): Pair<IntercomCommand?, String?> {
        val command = IntercomCommand.fromMessage(message)
        return if (command != null) {
            val extractedData = extractData(command, message)
            Pair(command, extractedData)
        } else {
            Pair(null, null) // Unknown command
        }
    }

    private fun extractData(command: IntercomCommand, message: String): String? {
        return when (command) {
            IntercomCommand.PROGRAMMING_PASSWORD -> {
                val match = IntercomCommand.PROGRAMMING_PASSWORD.pattern.toRegex().find(message)
                match?.groupValues?.get(1) // Extract the new password
            }
            IntercomCommand.ADD_ADMIN_NUMBER -> {
                val match = IntercomCommand.ADD_ADMIN_NUMBER.pattern.toRegex().find(message)
                match?.groupValues?.get(1) // Extracts the admin phone number
            }
            IntercomCommand.SET_CALLOUT_1,
            IntercomCommand.SET_CALLOUT_2,
            IntercomCommand.SET_CALLOUT_3 -> {
                val match = command.pattern.toRegex().find(message)
                match?.groupValues?.get(1) // Extract the callout number
            }
            IntercomCommand.SET_MIC_VOLUME,
            IntercomCommand.SET_SPEAKER_VOLUME -> {
                val match = command.pattern.toRegex().find(message)
                match?.groupValues?.get(1) // Extract the volume level
            }
            else -> null // For now, other commands may not have additional data to extract
        }
    }
}