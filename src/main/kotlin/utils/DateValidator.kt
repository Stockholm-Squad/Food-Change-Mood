package org.example.utils

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

class DateValidator {
    companion object {
        fun isValidDate(date: String): Boolean {
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE // Default format: yyyy-MM-dd
            return try {
                LocalDate.parse(date, formatter) // Try to parse the date
                true
            } catch (e: DateTimeParseException) {
                false
            }
        }

        fun getDateFromString(date: String): Date {
            val localDate = LocalDate.parse(date)
            val instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
            return Date.from(instant)
        }
    }


}