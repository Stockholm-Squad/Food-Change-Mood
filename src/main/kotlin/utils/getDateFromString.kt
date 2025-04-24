package org.example.utils

import kotlinx.datetime.LocalDate

fun getDateFromString(stringDate: String): Result<LocalDate> {
    return try {
        Result.success(LocalDate.parse(stringDate))
    } catch (e: Exception) {
        Result.failure(e)
    }
}