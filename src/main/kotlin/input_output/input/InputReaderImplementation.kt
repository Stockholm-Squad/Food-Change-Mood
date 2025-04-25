package org.example.input_output.input

import kotlinx.datetime.LocalDate
import org.example.utils.DateParser


class InputReaderImplementation(private val dateParser: DateParser) : InputReader {
    override fun readStringOrNull(): String? {
        return readlnOrNull()
    }

    override fun readDateOrNull(): LocalDate? {
        return dateParser.getDateFromString(readlnOrNull() ?: "").fold(
            onSuccess = { it },
            onFailure = { null }
        )
    }

    override fun readFloatOrNull(): Float? {
        return readlnOrNull()?.toFloatOrNull()
    }

}