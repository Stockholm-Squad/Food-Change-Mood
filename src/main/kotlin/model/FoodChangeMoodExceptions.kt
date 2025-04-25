package org.example.model

import org.example.utils.Constants

sealed class FoodChangeMoodExceptions(message: String) : Throwable(message) {

    sealed class ValidationException(override val message: String = Constants.INVALID_INPUT_MESSAGE) :
        FoodChangeMoodExceptions(message) {
        data class RowParsingException(override val message: String) : ValidationException(message)
    }

    sealed class IOException(override val message: String) : FoodChangeMoodExceptions(message) {
        data class WriteFailedException(override val message: String) : IOException(message)
        data class ReadFailedException(override val message: String = Constants.ERROR_WHILE_READING_FROM_FILE) :
            IOException(message)
    }

    sealed class LogicException(message: String) : FoodChangeMoodExceptions(message) {

        data class CanNotParseDateFromString(override val message: String = Constants.CAN_NOT_PARSE_DATE_FROM_STRING) :
            LogicException(message)

        data class NoMealsForGymHelperException(override val message: String = Constants.NO_MEALS_FOR_GYM_HELPER) :
            LogicException(message)

        data class ErrorFetchingMeals(override val message: String = Constants.ERROR_FETCHING_MEALS) :
            LogicException(message)

        data class NoMealsFound(override val message: String = Constants.NO_MEALS_FOUND) : LogicException(message)
        data class NoMealsForSoThinPeopleException(override val message: String = Constants.NO_MEAL_FOR_SO_THIN_PEOPLE) :
            LogicException(message)

        data class NoSeaFoodMealsFound(override val message: String = Constants.NO_SEA_FOOD_MEALS_FOUND) :
            LogicException(message)

        data class NoIraqiMeals(override val message: String = Constants.NO_MEALS_FOR_GYM_HELPER) :
            LogicException(message)
    }
}