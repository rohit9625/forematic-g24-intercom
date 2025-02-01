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
}