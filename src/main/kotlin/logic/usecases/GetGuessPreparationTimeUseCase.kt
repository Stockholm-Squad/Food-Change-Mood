package org.example.logic.usecases


import org.example.logic.usecases.model.GuessPreparationTimeState

class GetGuessPreparationTimeUseCase {
    fun guessGame(
        userGuess: Int,
        preparationTime:Int
    ): Result<GuessPreparationTimeState> {
        return when {
            userGuess == preparationTime -> Result.success(GuessPreparationTimeState.CORRECT)
            userGuess>preparationTime -> Result.success(GuessPreparationTimeState.TOO_HIGH)
           else -> Result.success(GuessPreparationTimeState.TOO_LOW)

        }
    }



}


