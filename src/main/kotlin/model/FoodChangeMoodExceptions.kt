package org.example.model

import org.example.utils.Constants

sealed class FoodChangeMoodExceptions(message: String) : Throwable(message) {
    data class ValidationException(override val message: String = Constants.INVALID_INPUT_MESSAGE) : FoodChangeMoodExceptions(message)

    sealed class IOException(override val message: String) : FoodChangeMoodExceptions(message) {
        data class WriteFailedException(override val message: String) : IOException(message)
        data class ReadFailedException(override val message: String) : IOException(message)
    }

    sealed class LogicException(message: String) : FoodChangeMoodExceptions(message) {
        data class NoMealsForGymHelperException(override val message: String = Constants.NO_MEALS_FOR_GYM_HELPER) : LogicException(message)
        data class ErrorFetchingMeals(override val message: String = Constants.ERROR_FETCHING_MEALS): LogicException(message)
        data class NoMealsFound(override val message: String= Constants.NO_MEALS_FOUND): LogicException(message)

    }
}