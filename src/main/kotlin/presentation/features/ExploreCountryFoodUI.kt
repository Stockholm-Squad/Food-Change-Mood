package org.example.presentation.features

import org.example.logic.usecases.GetCountryMealsUseCase

class ExploreCountryFoodUI(private val getCountriesFood: GetCountryMealsUseCase) {

    fun exploreCountryFoodCulture() {
        println("🌍 Let's take your taste buds on a world tour!")
        println("Enter the country you want to explore ::")
        val countryName = readln()


        println("----------------------------------Here are 20 random Meals of $countryName----------------------------------\n")
        get20RandomMealsByCountryName(countryName)
    }

    private fun get20RandomMealsByCountryName(countryName: String) {

        getCountriesFood.getMealsForCountry(countryName).onSuccess { allMeals ->
            allMeals.forEach {
                println("\nMeal Name: ${it.name}\nMeal Description: ${it.description}\nMeal Ingredients: ${it.ingredients}\nMeal preparation steps: ${it.steps}\n")

            }
        }.onFailure { println("No Such Country found") }

    }
}