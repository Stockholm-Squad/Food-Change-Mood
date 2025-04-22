package org.example.utils

import org.example.logic.model.Results
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

fun getDateFromString(stringDate: String): Results<Date> {
    //parse date with this format 2005-02-25
    return try {
        val date = Date.from(LocalDate.parse(stringDate).atStartOfDay(ZoneId.systemDefault()).toInstant())
        Results.Success(date)
    } catch (e: Exception) {
        return Results.Fail(e)
    }
}
