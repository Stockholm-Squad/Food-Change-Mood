package org.example.utils

import java.util.*

interface InputReader {
    fun readLineOrNull(): String?
    fun readDateOrNull(): Date?
    fun readFloatOrNull(): Float?
}