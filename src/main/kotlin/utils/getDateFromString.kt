package org.example.utils

import org.example.model.FoodChangeMoodExceptions
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

fun getDateFromString(stringDate: String): Result<Date> {
    //parse date with this format 2005-02-25
    return try {
        val date = Date.from(LocalDate.parse(stringDate).atStartOfDay(ZoneId.systemDefault()).toInstant())
        Result.success(date)
    } catch (e: Exception) {
        return Result.failure(FoodChangeMoodExceptions.LogicException.CantParseDateFromString())
    }
}
