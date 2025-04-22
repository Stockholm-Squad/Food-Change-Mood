package org.example.logic.usecases

import org.example.logic.usecases.model.GuessPreparationTimeState

class GetGuessPreparationTimeUseCase(
    private val getRandomMealUseCase: GetRandomMealUseCase
) {
    fun guessGame(
        userGuess: Int,
        attempts: Int
    ): Result<GuessPreparationTimeState> {
        return getRandomMealUseCase.getRandomMeal().fold(
            onSuccess = { meal ->
                val preparationTime = meal.minutes
                    ?: return Result.failure(IllegalStateException("Meal has no preparation time"))

                if (userGuess == preparationTime) {
                    Result.success(GuessPreparationTimeState.CORRECT)
                } else if (attempts >= MAX_ATTEMPT) {
                    Result.success(GuessPreparationTimeState.FAILED)
                } else if (userGuess < preparationTime) {
                    Result.success(GuessPreparationTimeState.TOO_LOW)
                } else {
                    Result.success(GuessPreparationTimeState.TOO_HIGH)
                }
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }


    companion object {
        private const val MAX_ATTEMPT = 3
    }
}


