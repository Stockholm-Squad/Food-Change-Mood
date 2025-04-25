package org.example.utils

import kotlinx.datetime.LocalDate

class DateParserImpl : DateParser {
    override fun getDateFromString(stringDate: String): Result<LocalDate> =
        runCatching { LocalDate.parse(stringDate) }
}