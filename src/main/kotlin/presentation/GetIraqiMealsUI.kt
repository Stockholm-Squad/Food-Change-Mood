package org.example.presentation

import logic.GetIraqiMealsUseCase

class GetIraqiMealsUI(
    private val getIraqiMealsUseCase: GetIraqiMealsUseCase
) {
    fun getIraqiMeals() {
        println("🍽 Ready for some amazing Iraqi meals? Let's go!")
        getIraqiMealsUseCase.getIraqiMales()
            .takeIf { meals->
                meals.isNotEmpty() }?.forEach { iraqiMeal ->
                println("Name: ${iraqiMeal.first}")
                println("Time: ${iraqiMeal.second}")
                println("Description: ${iraqiMeal.third ?: "No description available"}")
                println("------------------------------------------------------------------------------")
            } ?: throw IllegalStateException("Sorry, we don't have any iraqi meals available")
    }
}