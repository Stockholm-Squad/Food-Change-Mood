package org.example.logic.usecases

import model.Meal
import org.example.logic.usecases.model.GuessPreparationTimeState

class GetGuessPreparationTimeUseCase {
    fun guessGame(
        userGuess: Int,
        attempts: Int,
        preparationTime:Int
    ): Result<GuessPreparationTimeState> {
        return when {
            userGuess == preparationTime -> Result.success(GuessPreparationTimeState.CORRECT)
            attempts >= MAX_ATTEMPT -> Result.success(GuessPreparationTimeState.FAILED)
            userGuess < preparationTime -> Result.success(GuessPreparationTimeState.TOO_LOW)
            else -> Result.success(GuessPreparationTimeState.TOO_HIGH)
        }
    }



    companion object {
        private const val MAX_ATTEMPT = 3
    }
}


