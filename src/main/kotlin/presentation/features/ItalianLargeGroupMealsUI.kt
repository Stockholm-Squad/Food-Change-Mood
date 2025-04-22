package org.example.presentation.features

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
            it.forEach { meal ->
                println("${meal.id} -> ${meal.name}")
            }
        }.also { meals ->
            while (true) {
                println()
                println("-1 -> back")
                println("meal id -> view details")
                val input = readlnOrNull()
                val mealId = input?.toIntOrNull()

                if (mealId == null) {
                    println("Enter a valid ID or -1")
                    continue
                } else if (mealId == -1) {
                    break
                } else {
                    meals.viewMealInListDetails(mealId)
                }
            }
        }
    }
}