package org.example.presentation.features

import org.example.logic.usecases.GetGuessPreparationTimeUseCase
import org.example.logic.usecases.GetRandomMealUseCase
import org.example.logic.usecases.model.GuessPreparationTimeState

class GuessGameUI(
    private val getGuessPreparationTimeUseCase: GetGuessPreparationTimeUseCase,
    private val getRandomMealUseCase: GetRandomMealUseCase
) {
    fun playGuessGame() {
        getRandomMealUseCase.getRandomMeal().fold(
            onSuccess = { meal ->
                val correctTime = meal.minutes
                if (correctTime == null) {
                    println("❌ This meal has no preparation time.")
                    return
                }

                println("🎮 Guess the preparation time of: ${meal.name}")
                var attempts = 0

                while (attempts < MAX_ATTEMPTS) {
                    print("Enter your guess: ")
                    val userGuess = readLine()?.toIntOrNull()

                    if (userGuess == null) {
                        println("❗ Invalid input. Please enter a number.")
                        continue
                    }

                    val result = getGuessPreparationTimeUseCase.guessGame(
                        userGuess = userGuess,
                        attempts = attempts,
                        preparationTime = correctTime
                    )

                    when (result.getOrNull()) {
                        GuessPreparationTimeState.CORRECT -> {
                            println("🎉 Correct! The preparation time is $correctTime minutes.")
                            return
                        }
                        GuessPreparationTimeState.TOO_LOW -> println("⬇️ Too low.")
                        GuessPreparationTimeState.TOO_HIGH -> println("⬆️ Too high.")
                        GuessPreparationTimeState.FAILED -> {
                            println("❌ You've used all attempts. The correct time was $correctTime minutes.")
                            return
                        }
                        else -> println("⚠️ Unknown result.")
                    }

                    attempts++
                }
            },
            onFailure = { error ->
                println("⚠️ Error: ${error.message}")
            }
        )
    }


    companion object {
        private const val MAX_ATTEMPTS = 3
    }
}






