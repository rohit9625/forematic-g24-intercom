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
        assertEquals("1234", extractedData?.firstValue)
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
        assertEquals("07836975319", extractedData?.firstValue)
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
            assertEquals(expectedData, extractedData?.firstValue)
        }
    }

    @Test
    fun `test parseCommand with valid volume commands`() {
        val micVolumeCommand = "1234#MIC#04#"
        val speakerVolumeCommand = "1234#SP#03#"

        val (micCommand, micCommandData) = CommandParser.parseCommand(micVolumeCommand)
        val (speakerCommand, speakerCommandData) = CommandParser.parseCommand(speakerVolumeCommand)

        assertEquals(IntercomCommand.SET_MIC_VOLUME, micCommand)
        assertEquals(IntercomCommand.SET_SPEAKER_VOLUME, speakerCommand)
        assertEquals("04", micCommandData?.firstValue)
        assertEquals("03", speakerCommandData?.firstValue)
    }

    @Test
    fun `test parseCommand with valid timezone mode command`() {
        val messages = listOf("1234#HOLS#", "1234#FREE#", "1234#DAY#", "1234#NIGHT#")
        val modes = listOf("HOLS", "FREE", "DAY", "NIGHT")
        val expectedCommand = IntercomCommand.SET_TIMEZONE_MODE

        for ((message, expectedMode) in messages.zip(modes)) {
            val (command, extractedData) = CommandParser.parseCommand(message)

            assertEquals(expectedCommand, command)
            assertEquals(expectedMode, extractedData?.firstValue)
        }
    }

    @Test
    fun `parseCommand should correctly extract Relay 1 Output Name`() {
        val (command, extractedData) = CommandParser.parseCommand("1234#ID1#Big gate#")
        assertEquals(IntercomCommand.SET_RELAY1_OUTPUT_NAME, command)
        assertEquals("Big gate", extractedData?.firstValue)
    }

    @Test
    fun `parseCommand should correctly extract Relay 2 Output Name`() {
        val (command, extractedData) = CommandParser.parseCommand("1234#ID2#Jaguar#")
        assertEquals(IntercomCommand.SET_RELAY2_OUTPUT_NAME, command)
        assertEquals("Jaguar", extractedData?.firstValue)
    }

    @Test
    fun `parseCommand should reject Relay 1 Output Name with special characters`() {
        val (command, extractedData) = CommandParser.parseCommand("1234#ID1#Big$ Gate#")
        assertEquals(null, command)  // ❌ Should NOT match
        assertEquals(null, extractedData)
    }

    @Test
    fun `parseCommand should reject Relay 1 Output Name with numbers`() {
        val (command, extractedData) = CommandParser.parseCommand("1234#ID1#Gate123#")
        assertEquals(null, command)  // ❌ Should NOT match
        assertEquals(null, extractedData)
    }

    @Test
    fun `parseCommand should reject Relay 1 Output Name with empty name`() {
        val (command, extractedData) = CommandParser.parseCommand("1234#ID1##")
        assertEquals(null, command)  // ❌ Should NOT match
        assertEquals(null, extractedData)
    }

    @Test
    fun `parseCommand should correctly identify GET_Relay1_OUTPUT_NAME command`() {
        val (command, extractedData) = CommandParser.parseCommand("1234#ID1?#")
        assertEquals(IntercomCommand.GET_RELAY1_OUTPUT_NAME, command)
        assertEquals(null, extractedData)
    }

    @Test
    fun `parseCommand should correctly identify GET_Relay2_OUTPUT_NAME command`() {
        val (command, extractedData) = CommandParser.parseCommand("1234#ID2?#")
        assertEquals(IntercomCommand.GET_RELAY2_OUTPUT_NAME, command)
        assertEquals(null, extractedData)
    }

    @Test
    fun `parseCommand should correctly identify SET_RELAY1_TIME command`() {
        val (command, extractedData) = CommandParser.parseCommand("1234#RL1T#02#")
        assertEquals(IntercomCommand.SET_RELAY1_TIME, command)
        assertEquals("02", extractedData?.firstValue)
    }

    @Test
    fun `parseCommand should correctly identify SET_RELAY2_TIME command`() {
        val (command, extractedData) = CommandParser.parseCommand("1234#RL2T#10#")
        assertEquals(IntercomCommand.SET_RELAY2_TIME, command)
        assertEquals("10", extractedData?.firstValue)
    }

    @Test
    fun `parseCommand should correctly identify FIND_NEXT_RELAY1_LOCATION command`() {
        val (command, data) = CommandParser.parseCommand("1234#R1A?#")
        assertEquals(IntercomCommand.FIND_NEXT_RELAY1_LOCATION, command)
        assertEquals(null, data)
    }

    @Test
    fun `parseCommand should correctly identify FIND_NEXT_RELAY2_LOCATION command`() {
        val (command, data) = CommandParser.parseCommand("1234#R2A?#")
        assertEquals(IntercomCommand.FIND_NEXT_RELAY2_LOCATION, command)
        assertEquals(null, data)
    }

    @Test
    fun `parseCommand should identity FIND_NEXT_CLI_LOCATION command`() {
        val (command, data) = CommandParser.parseCommand("1234#CIA?#")
        assertEquals(IntercomCommand.FIND_NEXT_CLI_LOCATION, command)
        assertEquals(null, data)
    }

    @Test
    fun `parseCommand should identify FIND_DELIVERY_CODE_LOCATION command`() {
        val (command, data) = CommandParser.parseCommand("1234#SUA?#")
        assertEquals(IntercomCommand.FIND_DELIVERY_CODE_LOCATION, command)
        assertEquals(null, data)
    }

    @Test
    fun `parseCommand should correctly identify SET_KEYPAD_CODE command`() {
        val (command, data) = CommandParser.parseCommand("1234#11#001#1066#")
        assertEquals(IntercomCommand.SET_RELAY_KEYPAD_CODE, command)
        assertEquals("001", data?.firstValue) // For location parameter
        assertEquals("1066", data?.secondValue) // For code parameter
    }

    @Test
    fun `parseCommand should correctly identify SET_CLI_MODE command`() {
        val (command1, data1) = CommandParser.parseCommand("1234#ANY#")
        val (command2, data2) = CommandParser.parseCommand("1234#AUTH#")

        assertEquals(IntercomCommand.SET_CLI_MODE, command1)
        assertEquals(IntercomCommand.SET_CLI_MODE, command2)
        assertEquals("ANY", data1?.firstValue)
        assertEquals("AUTH", data2?.firstValue)
    }
}