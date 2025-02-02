package com.forematic.intercom.utils

import org.junit.Assert
import org.junit.Test

class PhoneNumberUtilsTest {
    private val baseNumber = "1231231234"
    private val formattedNumber = "+91 123-123-1234"
    private val baseNumberWithCountryCode = "+91$baseNumber"
    private val baseNumberWithLeadingZero = "0$baseNumber"
    private val differentNumber = "+918802201234"

    @Test
    fun `normalizePhoneNumber should remove non-digit characters`() {
        val actual = PhoneNumberUtils.normalizePhoneNumber(formattedNumber)

        Assert.assertEquals(baseNumber, actual)
    }

    @Test
    fun `normalizePhoneNumber should remove leading zeros`() {
        val actual = PhoneNumberUtils.normalizePhoneNumber(baseNumberWithLeadingZero)

        Assert.assertEquals(baseNumber, actual)
    }

    @Test
    fun `normalizePhoneNumber should remove country code`() {
        val actual = PhoneNumberUtils.normalizePhoneNumber(baseNumberWithCountryCode)

        Assert.assertEquals(baseNumber, actual)
    }

    @Test
    fun `normalizePhoneNumber should handle number without any format`() {
        val actual = PhoneNumberUtils.normalizePhoneNumber(baseNumber)

        Assert.assertEquals(baseNumber, actual)
    }

    @Test
    fun `arePhoneNumbersEqual should return true for equal numbers with different formats`() {
        val result1 = PhoneNumberUtils.arePhoneNumbersEqual(baseNumberWithLeadingZero, baseNumberWithCountryCode)
        val result2 = PhoneNumberUtils.arePhoneNumbersEqual(formattedNumber, baseNumber)
        val result3 = PhoneNumberUtils.arePhoneNumbersEqual(baseNumberWithCountryCode, baseNumber)
        val result4 = PhoneNumberUtils.arePhoneNumbersEqual(baseNumberWithLeadingZero, baseNumber)

        Assert.assertTrue(result1)
        Assert.assertTrue(result2)
        Assert.assertTrue(result3)
        Assert.assertTrue(result4)
    }

    @Test
    fun `arePhoneNumbersEqual should return false for different numbers`() {
        val result1 = PhoneNumberUtils.arePhoneNumbersEqual(formattedNumber, differentNumber)
        val result2 = PhoneNumberUtils.arePhoneNumbersEqual(baseNumber, differentNumber)
        val result3 = PhoneNumberUtils.arePhoneNumbersEqual(baseNumberWithLeadingZero, differentNumber)

        Assert.assertFalse(result1)
        Assert.assertFalse(result2)
        Assert.assertFalse(result3)
    }
}