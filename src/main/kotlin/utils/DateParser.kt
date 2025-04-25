package org.example.utils

import kotlinx.datetime.LocalDate

interface DateParser {
    fun getDateFromString(stringDate: String): Result<LocalDate>
}