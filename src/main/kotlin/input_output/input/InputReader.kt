package org.example.input_output.input

import java.util.Date

interface InputReader {
    fun readLineOrNull(): String?
    fun readDateOrNull(): Date?
    fun readFloatOrNull(): Float?
    fun readLine(): String
}