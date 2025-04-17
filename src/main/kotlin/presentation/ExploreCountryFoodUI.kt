package org.example.presentation

import org.example.logic.GetCountriesFoodUseCase

class ExploreCountryFoodUI(private val getCountriesFood: GetCountriesFoodUseCase) {

    fun exploreCountryFoodCulture() {
        // val getCountriesFoodUseCase = GetCountriesFoodUseCase(mealsRepository)
        println("🌍 Let's take your taste buds on a world tour!")
        println("Enter the country you want to explore ::")
        val countryName = readlnOrNull()
        val meals = getCountriesFood.getRandomMeals(countryName!!)

        println("----------------------------------Here are 20 random Meals of $countryName----------------------------------\n")
        meals.forEach {
            println("\nMeal Name: ${it.name}\nMeal Description: ${it.description}\nMeal Ingredients: ${it.ingredients}\nMeal preparation steps: ${it.steps}\n")
        }
    }
}