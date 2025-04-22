package org.example.presentation.features

import org.example.logic.model.Results
import org.example.logic.usecases.GetCountryFoodUseCase

class ExploreCountryFoodUI(private val getCountriesFood: GetCountryFoodUseCase) {

    fun exploreCountryFoodCulture() {
        // val getCountriesFoodUseCase = GetCountriesFoodUseCase(mealsRepository)
        println("🌍 Let's take your taste buds on a world tour!")
        println("Enter the country you want to explore ::")
        val countryName = readln()


        println("----------------------------------Here are 20 random Meals of $countryName----------------------------------\n")
        get20RandomMealsByCountryName(countryName)
    }

    private fun get20RandomMealsByCountryName(countryName: String) {


        when (val result = getCountriesFood.getRandomMealsForCountry(countryName)) {
            is Results.Success -> result.model.forEach {
                println("\nMeal Name: ${it.name}\nMeal Description: ${it.description}\nMeal Ingredients: ${it.ingredients}\nMeal preparation steps: ${it.steps}\n")
            }

            is Results.Fail -> println("No Such Country found")
        }
    }
}