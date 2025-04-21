package org.example.model


sealed class Result<out T> {
    data class Success<T>(val value: T) : Result<T>()
    data class Failure(val errorMessage: String, val cause: Throwable? = null) : Result<Nothing>()
}