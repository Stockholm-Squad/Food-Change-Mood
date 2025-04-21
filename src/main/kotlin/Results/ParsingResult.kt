package org.example.Results

// Custom Result class for better error handling
sealed class ParsingResult<out T> {
    data class Success<T>(val value: T) : ParsingResult<T>()
    data class Failure(val errorMessage: String, val cause: Throwable? = null) : ParsingResult<Nothing>()
}