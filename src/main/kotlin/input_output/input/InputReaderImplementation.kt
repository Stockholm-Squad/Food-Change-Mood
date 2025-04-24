package org.example.input_output.input

import org.example.utils.getDateFromString
import java.util.*

class InputReaderImplementation : InputReader {
    override fun readLineOrNull(): String? {
        return readlnOrNull()
    }

    override fun readDateOrNull(): Date? {
        return getDateFromString(readlnOrNull() ?: "").fold(
            onSuccess = { it },
            onFailure = { null }
        )
    }

    override fun readFloatOrNull(): Float? {
        return readlnOrNull()?.toFloatOrNull()
    }

    override fun readIntOrNull(): Int? {
        return readlnOrNull()?.toIntOrNull()
    }

}