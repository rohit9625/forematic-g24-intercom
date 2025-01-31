package com.forematic.intercom.utils

object MessageUtils {
    // Pattern: "<old_password>#PWD#<new_password>#"
    private const val PROGRAMMING_PASSWORD_PATTERN = "\\d{4}#PWD#(\\d{4})#"

    fun extractPassword(message: String): String? {
        // Example pattern: "1234#PWD#8888#"
        val match = PROGRAMMING_PASSWORD_PATTERN.toRegex().find(message)
        return match?.groupValues?.get(1) // Extract the new password
    }

    fun isValidProgrammingPassword(password: String): Boolean {
        return password.matches(PROGRAMMING_PASSWORD_PATTERN.toRegex())
    }
}