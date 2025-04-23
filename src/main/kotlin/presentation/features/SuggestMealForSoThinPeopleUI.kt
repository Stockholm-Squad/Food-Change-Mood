package org.example.presentation.features

import model.Meal
import org.example.logic.usecases.GetMealForSoThinPeopleUseCase


class SuggestMealForSoThinPeopleUI(
    private val getMealForSoThinPeopleUseCase: GetMealForSoThinPeopleUseCase,
) {
    fun getMealWithHighCalorie() {
        println("-------------------------------------------------------------------------------------------------")
        println("🔥 Feeling too thin? This meal above 700 calories!")
        getMealForSoThinPeopleUseCase.suggestRandomMealForSoThinPeople()
            .fold(
                onSuccess = { meal ->
                    println("------------------------------------------------------------------------------------------------")
                    println("Name: " + meal.name)
                    println("Description: " + meal.description)
                    println("--------------------------------------------------------------------------------------------------")
                    println("Do you like it? (yes/no) 😊")
                    when (readlnOrNull()?.trim()?.lowercase()) {
                        "no" -> getMealWithHighCalorie()
                        "yes" -> showMealDetails(meal)
                        else -> println("Invalid input! Expected yes or no.")
                    }
                },
                onFailure = { exception ->
                    println(exception)
                }
            )
    }

    private fun showMealDetails(meal: Meal) {
        println("Meal Name: " + meal.name)
        println("Meal Description: " + meal.description)
        println("Meal Preparation Time: " + meal.minutes + " minutes")
        println("Meal " + meal.nutrition)

    }
}
