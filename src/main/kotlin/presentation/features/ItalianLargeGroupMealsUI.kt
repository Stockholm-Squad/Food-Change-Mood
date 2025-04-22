package org.example.presentation.features

import model.Meal
import org.example.logic.usecases.GetItalianMealsForLargeGroupUseCase
import org.example.utils.viewMealInListDetails

class ItalianLargeGroupMealsUI(private val getItalianMealsForLargeGroupUseCase: GetItalianMealsForLargeGroupUseCase) {

    fun italianLargeGroupMealsUI() {
        println("🍝 Planning a big Italian feast? Here's a list of meals perfect for large groups:")
        println("Loading...")
        getItalianMealsForLargeGroupUseCase.getMeals().fold(
            onSuccess = { meals -> meals },
            onFailure = { exception ->
                println("error: " + exception)
                emptyList()
            }
        ).also {
            displayMealsAndHandleInteraction(it)
        }
    }

    private fun printMealsIdName(mealsList: List<Meal>) {
        mealsList.forEach { meal ->
            println("${meal.id} -> ${meal.name}")
        }
    }

    private fun displayMealsAndHandleInteraction(meals: List<Meal>) {
        printMealsIdName(meals)

        while (true) {
            println()
            println("-1 -> back")
            println("meal id -> view details")
            val input = readlnOrNull()
            val mealId = input?.toIntOrNull()

            when {
                mealId == null -> println("Enter a valid ID or -1")
                mealId == -1 -> break
                else -> meals.viewMealInListDetails(mealId)
            }
        }
    }
}