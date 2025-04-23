package org.example.model

sealed class FoodChangeMoodExceptions(message:String):Throwable(message) {
    sealed class ValidationException(override val message: String) : FoodChangeMoodExceptions(message)
    sealed class IOException(override val message: String) : FoodChangeMoodExceptions(message) {
        class WriteFailedException(message: String) : IOException(message)
        class ReadFailedException(message: String):IOException(message)
    }
    sealed class LogicException(message: String):FoodChangeMoodExceptions(message)
}