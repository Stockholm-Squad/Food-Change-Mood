package org.example.model

import org.example.utils.Constants

sealed class FoodChangeMoodExceptions(message: String) : Throwable(message) {
    sealed class ValidationException(override val message: String) : FoodChangeMoodExceptions(message){
        data class RowParsingException(override val message: String): ValidationException(message)
    }

    sealed class IOException(override val message: String) : FoodChangeMoodExceptions(message) {
        data class WriteFailedException(override val message: String) : IOException(message)
        data class ReadFailedException(override val message: String = Constants.ERROR_WHILE_READING_FROM_FILE) :
            IOException(message)
    }

    sealed class LogicException(message: String) : FoodChangeMoodExceptions(message) {
        data class NoMealsForGymHelperException(override val message: String = Constants.NO_MEALS_FOR_GYM_HELPER) :
            LogicException(message)
    }
}