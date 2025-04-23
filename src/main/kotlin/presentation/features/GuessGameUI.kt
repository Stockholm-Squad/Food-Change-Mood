package org.example.presentation.features

import org.example.logic.usecases.GetGuessPreparationTimeUseCase
import org.example.logic.usecases.GetRandomMealUseCase
import org.example.logic.usecases.model.GuessPreparationTimeState
class GuessGameUI(
    private val getGuessPreparationTimeUseCase: GetGuessPreparationTimeUseCase,
    private val getRandomMealUseCase: GetRandomMealUseCase,
    private val inputProvider: () -> String = { readLine() ?: "" },
    private val outputCollector: (String) -> Unit = { println(it) }
) {
    fun playGuessGame() {
        getRandomMealUseCase.getRandomMeal().fold(
            onSuccess = { meal ->
                val correctTime = meal.minutes
                outputCollector("🎮 Guess the preparation time of: ${meal.name}")
                var attempts = 0
                while (attempts < MAX_ATTEMPTS) {
                    outputCollector("Enter your guess: ")
                    val userGuess = inputProvider().toInt()
                    val result = getGuessPreparationTimeUseCase.guessGame(
                        userGuess = userGuess,
                        attempts = attempts,
                        preparationTime = correctTime
                    )

                    when (result.getOrNull()) {
                        GuessPreparationTimeState.CORRECT -> {
                            outputCollector("🎉 Correct! The preparation time is $correctTime minutes.")
                            return
                        }
                        GuessPreparationTimeState.TOO_LOW -> outputCollector("⬇️ Too low.")
                        GuessPreparationTimeState.TOO_HIGH -> outputCollector("⬆️ Too high.")
                          else-> {
                            outputCollector("❌ You've used all attempts. The correct time was $correctTime minutes.")
                            return
                        }
                    }

                    attempts++
                }
            },
            onFailure = { error ->
                outputCollector("Error: ${error.message ?: "Something went wrong"}")
            }
        )
    }

    companion object {
        private const val MAX_ATTEMPTS = 3
    }
}







