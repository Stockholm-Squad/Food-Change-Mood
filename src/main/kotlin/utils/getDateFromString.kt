package org.example.utils

import kotlinx.datetime.LocalDate
import org.example.model.FoodChangeMoodExceptions

fun getDateFromString(stringDate: String): Result<LocalDate> {
    return try {
        val localDate = LocalDate.parse(stringDate)
        Result.success(localDate)
    } catch (e: Exception) {
        Result.failure(FoodChangeMoodExceptions.LogicException.CanNotParseDateFromString())
    }
}
