package org.example.presentation.features

import org.example.logic.model.Results
import org.example.logic.usecases.GetGuessGameUseCase

class GuessGameUI(
    private val getGuessGameUseCase: GetGuessGameUseCase
) {
    fun playGuessGame() {

        val mealResult = getGuessGameUseCase.getRandomMeal()
        when (mealResult) {
            is Results.Success -> {
                val correctTime = mealResult.model.minutes
                if (correctTime == null) {
                    println("❌ This meal has no preparation time.")
                    return
                }
                var attempts = 3
                println("🎮 Guess the preparation time of: ${mealResult.model.name}")
                while (attempts > 0) {
                    print("Enter your guess: ")
                    val guessUser: Int? = readLine()?.toIntOrNull()
                    if (correctTime == guessUser) {
                        println("🎉 Correct! The preparation time is $correctTime minutes.")
                        return
                    } else if (guessUser!! < correctTime!!) {
                        println("⬇️ Too low.")

                    } else if (guessUser!! > correctTime) {
                        println("⬆️ Too high.")
                    }
                    attempts--
                }
                println("❌ You've used all attempts. The correct time was $correctTime minutes.")
            }

            is Results.Fail -> {
                println("⚠️ Error: ${mealResult.exception.message}")
            }
        }
    }
}




