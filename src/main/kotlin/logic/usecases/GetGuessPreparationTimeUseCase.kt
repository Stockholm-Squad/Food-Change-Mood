package org.example.logic.usecases


import org.example.logic.usecases.model.GuessPreparationTimeState

class GetGuessPreparationTimeUseCase {
    fun guessGame(
        userGuess: Int?,
        preparationTime: Int?
    ): Result<GuessPreparationTimeState> {
        if (userGuess == null || preparationTime == null) {
            return Result.success(GuessPreparationTimeState.FAILED)
        }

        return when {
            userGuess == preparationTime -> Result.success(GuessPreparationTimeState.CORRECT)
            userGuess > preparationTime -> Result.success(GuessPreparationTimeState.TOO_HIGH)
            userGuess < preparationTime -> Result.success(GuessPreparationTimeState.TOO_LOW)
            else -> Result.success(GuessPreparationTimeState.FAILED)
        }
    }

}


