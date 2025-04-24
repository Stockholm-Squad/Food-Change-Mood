package org.example.input_output.input

import org.example.utils.getDateFromString
import java.util.*

class InputReaderImplementation : InputReader {
    override fun readStringOrNull(): String? {
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

}