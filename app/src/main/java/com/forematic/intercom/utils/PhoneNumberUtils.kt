package com.forematic.intercom.utils

object PhoneNumberUtils {
    fun normalizePhoneNumber(phoneNumber: String): String {
        val digitsOnly = phoneNumber.replace(Regex("\\D"), "")
        val noLeadingZeros = digitsOnly.replace(Regex("^0+"), "")
        val noCountryCode = if(noLeadingZeros.startsWith("91") || noLeadingZeros.startsWith("44")) {
            noLeadingZeros.substring(2)
        } else { noLeadingZeros }

        return noCountryCode
    }

    fun arePhoneNumbersEqual(phoneNumber1: String, phoneNumber2: String): Boolean {
        return normalizePhoneNumber(phoneNumber1) == normalizePhoneNumber(phoneNumber2)
    }
}