package com.forematic.intercom.utils

import junit.framework.TestCase.assertEquals
import org.junit.Assert
import org.junit.Test

class MessageUtilsTest {
    @Test
    fun `extractPassword should return correct password when pattern matches`() {
        // Arrange
        val message = "1234#PWD#8888#"
        val expectedPassword = "8888"

        // Act
        val actualPassword = MessageUtils.extractPassword(message)

        // Assert
        assertEquals(expectedPassword, actualPassword)
    }

    @Test
    fun `extractPassword should return null when pattern does not match`() {
        // Arrange
        val message = "invalid message"

        // Act
        val actualPassword = MessageUtils.extractPassword(message)

        // Assert
        Assert.assertNull(actualPassword)
    }

    @Test
    fun `isValidProgrammingPassword should return true when password matches pattern`() {
        // Arrange
        val password = "1234#PWD#8888#"

        // Act
        val isValid = MessageUtils.isValidProgrammingPassword(password)

        // Assert
        Assert.assertTrue(isValid)
    }

    @Test
    fun `isValidProgrammingPassword should return false when password does not match pattern`() {
        // Arrange
        val password = "invalid password"

        // Act
        val isValid = MessageUtils.isValidProgrammingPassword(password)

        // Assert
        Assert.assertFalse(isValid)
    }

    @Test
    fun `isValidProgrammingPassword should return false when password has invalid format`() {
        // Arrange
        val password = "1234#PWD#888#"

        // Act
        val isValid = MessageUtils.isValidProgrammingPassword(password)

        // Assert
        Assert.assertFalse(isValid)
    }
}