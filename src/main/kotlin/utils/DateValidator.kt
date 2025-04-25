package org.example.utils

import kotlinx.datetime.LocalDate


class DateValidator {
    fun isValidDate(date: String): Boolean {
        return try {
            LocalDate.parse(date)
            true
        } catch (e: Exception) {
            false
        }
    }
}