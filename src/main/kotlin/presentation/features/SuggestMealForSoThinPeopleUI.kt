package org.example.presentation.features

import model.Meal
import org.example.logic.usecases.GetMealsForSoThinProblemUseCase


class SuggestMealForSoThinPeopleUI(
    private val getMealsForSoThinProblemUseCase: GetMealsForSoThinProblemUseCase,
) {
    fun getMaleWithHighCalorie() {
        println("-------------------------------------------------------------------------------------------------")
        println("🔥 Feeling too thin? This meal above 700 calories!")
        getMealsForSoThinProblemUseCase.suggestRandomMealForSoThinPeople()
            .fold(
                onSuccess = { meal ->
                    println("------------------------------------------------------------------------------------------------")
                    println("Name: " + meal.name)
                    println("Description: " + meal.description)
                    println("--------------------------------------------------------------------------------------------------")
                    println("Do you like it? (yes/no) 😊")
                    when (readlnOrNull()?.trim()?.lowercase()) {
                        "no" -> getMaleWithHighCalorie()
                        "yes" -> displayFullDetails(meal)
                        else -> println("Invalid input! Expected yes or no.")
                    }
                },
                onFailure = { exception ->
                    println(exception)
                    null
                }
            )

    }

    private fun displayFullDetails(meal: Meal) {
        println("Meal Name: " + meal.name)
        println("Meal Description: " + meal.description)
        println("Meal Preparation Time: " + meal.minutes + " minutes")
        println("Meal " + meal.nutrition)

    }
}
