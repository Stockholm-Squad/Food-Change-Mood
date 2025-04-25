package org.example.utils

class DateValidator {
    fun isValidDate(date: String): Boolean {
        return try {
            kotlinx.datetime.LocalDate.parse(date)
            true
        } catch (e: Exception) {
            false
        }
    }
}