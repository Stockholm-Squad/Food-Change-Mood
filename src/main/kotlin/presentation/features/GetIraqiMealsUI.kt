package org.example.presentation.features

import org.example.logic.usecases.GetIraqiMealsUseCase

class GetIraqiMealsUI(
    private val getIraqiMealsUseCase: GetIraqiMealsUseCase
) {
    fun getIraqiMeals() {
        println("🍽 Ready for some amazing Iraqi meals? Let's go!")
        getIraqiMealsUseCase.getIraqiMeals().fold(
            onSuccess = { meals ->
                meals.forEach { iraqiMeal ->
                    println("Name: ${iraqiMeal.name}")
                println("Time: ${iraqiMeal.minutes}")
                println("Description: ${iraqiMeal.description ?: "No description available"}")
                println("------------------------------------------------------------------------------")
                }
            },
            onFailure = { println("no iraqi meals found") }
        )

        }
}