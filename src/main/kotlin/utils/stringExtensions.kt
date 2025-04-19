package org.example.utils

import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

fun String.parseDate(): Date? {
    //parse date with this format 2005-02-25
    return try {
        Date.from(LocalDate.parse(this).atStartOfDay(ZoneId.systemDefault()).toInstant())
    } catch (e: Exception) {
        return null
    }

}


//TODO make the file name to be general  ex: Extensions