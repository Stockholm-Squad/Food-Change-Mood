package org.example.presentation

import org.example.logic.GetCountriesFoodUseCase
import org.example.logic.MealsRepository

class ExploreCountryFoodUI(private val mealsRepository: MealsRepository) {

    fun exploreCountryFoodCulture() {
        // val getCountriesFoodUseCase = GetCountriesFoodUseCase(mealsRepository)
        println("🌍 Let's take your taste buds on a world tour!")
        println("Enter the country you want to explore ::")
        val countryName = readlnOrNull()
        val meals = GetCountriesFoodUseCase(mealsRepository).getRandomMeals(countryName!!)

        println("----------------------------------Here are 20 random Meals of $countryName----------------------------------\n")
        meals.forEach {
            println("\nMeal Name: ${it.name}\nMeal Description: ${it.description}\nMeal Ingredients: ${it.ingredients}\nMeal preparation steps: ${it.steps}\n")
        }
    }
}