package org.example.logic

import model.Meal

class GetGuessGameUseCase(
    private val mealsRepository: MealsRepository
) {

   private fun getRandomMeal(): Meal {
        val meals = mealsRepository.getAllMeals()
        return meals.random()
    }
    val meal =getGuessGameUseCase.getRandomMeal()
    fun GuessGame() {
        println("🎲  Can you guess the right the preparation time (in minutes) for the meal: ${meal.name}?")
        val correctTime=meal.minutes
        var attempts = 3
        while (attempts > 0) {
            print("Enter your guess: ")
            val guess = readLine()?.toIntOrNull()

            if (guess == null) {
                println("Invalid input. Please enter a number.")
                continue
            }

            if (guess == correctTime) {
                println("🎉 Correct! The preparation time is $correctTime minutes.")
                return
            } else if (guess < correctTime!!) {
                println("Too low.")
            } else {
                println("Too high.")
            }

            attempts--
        }

        println("❌ You've used all attempts. The correct time was $correctTime minutes.")

  
    
}