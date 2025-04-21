package org.example.presentation.features

import org.example.logic.usecases.GetMealsForLargeGroupUseCase
import org.example.utils.viewMealInListDetails

class ItalianLargeGroupMealsUI(private val getMealsForLargeGroupUseCase: GetMealsForLargeGroupUseCase) {

    fun italianLargeGroupMealsUI() {
        println("🍝 Planning a big Italian feast? Here's a list of meals perfect for large groups:")
        println("Loading...")
        val filteredList = getMealsForLargeGroupUseCase.getItalianMealsForLargeGroup()
        filteredList.forEach { meal ->
            println("${meal.id} -> ${meal.name}")
        }

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
                filteredList.viewMealInListDetails(mealId)
            }
        }
    }
}