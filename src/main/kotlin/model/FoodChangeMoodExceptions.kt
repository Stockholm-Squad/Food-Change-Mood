package org.example.model

sealed class FoodChangeMoodExceptions(message:String):Throwable(message) {
    sealed class ValidationException(override val message: String) : FoodChangeMoodExceptions(message)
    sealed class IOException(override val message: String) : FoodChangeMoodExceptions(message) {
       data class WriteFailedException(override val message: String) : IOException(message)
        data   class ReadFailedException(override val message: String):IOException(message)
    }
    sealed class LogicException(message: String):FoodChangeMoodExceptions(message){
        data class NoMealsFound(override val message: String = "No seafood meals found."): LogicException(message)
    }
}