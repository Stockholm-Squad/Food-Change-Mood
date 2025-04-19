package org.example.presentation

import org.example.logic.usecases.GetIraqiMealsUseCase

class GetIraqiMealsUI(
    private val getIraqiMealsUseCase: GetIraqiMealsUseCase
) {
    fun getIraqiMeals() {
        println("🍽 Ready for some amazing Iraqi meals? Let's go!")
        getIraqiMealsUseCase.getIraqiMeals()
            .takeIf { meals->
                meals.isNotEmpty() }?.forEach { iraqiMeal ->
                println("Name: ${iraqiMeal.name}")
                println("Time: ${iraqiMeal.minutes}")
                println("Description: ${iraqiMeal.description ?: "No description available"}")
                println("------------------------------------------------------------------------------")
            } ?: println("Sorry, we don't have any iraqi meals available")
    }
}