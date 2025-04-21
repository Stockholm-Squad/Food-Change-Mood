package org.example.Results


sealed class ReaderResult<out T> {
    data class Success<T>(val value: T) : ReaderResult<T>()
    data class Failure(val errorMessage: String, val cause: Throwable? = null) : ReaderResult<Nothing>()
}