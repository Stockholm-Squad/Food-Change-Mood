package org.example.utils

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

}