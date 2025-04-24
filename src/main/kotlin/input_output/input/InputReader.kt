package org.example.input_output.input

import java.util.Date

interface InputReader {
    fun readStringOrNull(): String?
    fun readDateOrNull(): Date?
    fun readFloatOrNull(): Float?
}