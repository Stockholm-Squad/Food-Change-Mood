package org.example.utils


class DateValidator(private val dateParser: DateParser) {
    fun isValidDate(date: String): Boolean {
        return dateParser.getDateFromString(date).fold(
            onSuccess = { true },
            onFailure = { false }
        )
    }
}