package org.example.input_output.input

import kotlinx.datetime.LocalDate
import org.example.utils.DateParser
import org.example.utils.DateParserImpl


class InputReaderImplementation : InputReader {
    override fun readStringOrNull(): String? {
        return readlnOrNull()
    }

    override fun readDateOrNull(): LocalDate? {
        return DateParserImpl().getDateFromString(readlnOrNull() ?: "").fold(
            onSuccess = { it },
            onFailure = { null }
        )
    }

    override fun readFloatOrNull(): Float? {
        return readlnOrNull()?.toFloatOrNull()
    }

}