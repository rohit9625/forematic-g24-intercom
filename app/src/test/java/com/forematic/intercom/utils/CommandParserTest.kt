package com.forematic.intercom.utils

import com.forematic.intercom.data.model.IntercomCommand
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CommandParserTest {
    @Test
    fun `test parseCommand with valid programmingPassword command`() {
        val message = "1234#PWD#1234#"
        val (command, extractedData) = CommandParser.parseCommand(message)

        assertEquals(IntercomCommand.PROGRAMMING_PASSWORD, command)
        assertEquals("1234", extractedData)
    }

    @Test
    fun `test parseCommand with valid requestSignalStrength command`() {
        val message = "1234#RSSI?#"
        val (command, extractedData) = CommandParser.parseCommand(message)

        assertEquals(IntercomCommand.REQUEST_SIGNAL_STRENGTH, command)
        assertEquals(null, extractedData)
    }

    @Test
    fun `test parseCommand with valid adminNumber command`() {
        val message = "1234#00#07836975319#"
        val (command, extractedData) = CommandParser.parseCommand(message)

        assertEquals(IntercomCommand.ADD_ADMIN_NUMBER, command)
        assertEquals("07836975319", extractedData)
    }

    @Test
    fun `test parseCommand with valid callout number commands`() {
        val messages = listOf(
            "1234#01#07836975319#",
            "1234#02#07836975319#",
            "1234#03#07836975319#"
        )
        val expectedCommands = listOf(
            IntercomCommand.SET_CALLOUT_1,
            IntercomCommand.SET_CALLOUT_2,
            IntercomCommand.SET_CALLOUT_3
        )
        val expectedData = "07836975319"

        for ((message, expectedCommand) in messages.zip(expectedCommands)) {
            val (command, extractedData) = CommandParser.parseCommand(message)

            assertEquals(expectedCommand, command)
            assertEquals(expectedData, extractedData)
        }
    }

    @Test
    fun `test parseCommand with valid volume commands`() {
        val micVolumeCommand = "1234#MIC#04#"
        val speakerVolumeCommand = "1234#SP#03#"

        val (micCommand, micVolume) = CommandParser.parseCommand(micVolumeCommand)
        val (speakerCommand, speakerVolume) = CommandParser.parseCommand(speakerVolumeCommand)

        assertEquals(IntercomCommand.SET_MIC_VOLUME, micCommand)
        assertEquals(IntercomCommand.SET_SPEAKER_VOLUME, speakerCommand)
        assertEquals("04", micVolume)
        assertEquals("03", speakerVolume)
    }

    @Test
    fun `test parseCommand with valid timezone mode command`() {
        val messages = listOf("1234#HOLS#", "1234#FREE#", "1234#DAY#", "1234#NIGHT#")
        val modes = listOf("HOLS", "FREE", "DAY", "NIGHT")
        val expectedCommand = IntercomCommand.SET_TIMEZONE_MODE

        for ((message, expectedMode) in messages.zip(modes)) {
            val (command, actualMode) = CommandParser.parseCommand(message)

            assertEquals(expectedCommand, command)
            assertEquals(expectedMode, actualMode)
        }
    }
}