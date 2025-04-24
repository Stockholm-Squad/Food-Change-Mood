package org.example.input_output.input

import kotlinx.datetime.LocalDate
import org.example.utils.getDateFromString

class InputReaderImplementation : InputReader {
    override fun readStringOrNull(): String? {
        return readlnOrNull()
    }

    override fun readDateOrNull(): LocalDate? {
        return getDateFromString(readlnOrNull() ?: "").fold(
            onSuccess = { it },
            onFailure = { null }
        )
    }

    override fun readFloatOrNull(): Float? {
        return readlnOrNull()?.toFloatOrNull()
    }

}