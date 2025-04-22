package org.example.presentation.features

import org.example.logic.usecases.GetGuessGameUseCase

class GuessGameUI(
    private val getGuessGameUseCase: GetGuessGameUseCase
) {
    fun playGuessGame() {

        val mealResult = getGuessGameUseCase.getRandomMeal()
        mealResult.fold(
            onSuccess = { mealResult ->
                val correctTime = mealResult.minutes
                if (correctTime == null) {
                    println("❌ This meal has no preparation time.")
                    return
                }
                var attempts = 3
                println("🎮 Guess the preparation time of: ${mealResult.name}")
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
            }, onFailure = { error->
                println("⚠️ Error:${error.message} ")

            }
        )


    }
}




