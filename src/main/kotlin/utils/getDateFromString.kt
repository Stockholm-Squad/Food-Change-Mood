package org.example.utils

import java.time.LocalDate
import java.time.ZoneId
import java.util.*

fun getDateFromString(stringDate: String): Date? {
    //parse date with this format 2005-02-25
    return try {
        Date.from(LocalDate.parse(stringDate).atStartOfDay(ZoneId.systemDefault()).toInstant())
    } catch (e: Exception) {
        return null
    }
}
