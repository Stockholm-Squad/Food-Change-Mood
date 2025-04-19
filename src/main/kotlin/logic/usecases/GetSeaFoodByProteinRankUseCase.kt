package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository

class GetSeaFoodByProteinRankUseCase(private val mealsRepository: MealsRepository) {
    fun getSeaFoodByProteinRank(): List<Meal> {
        return mealsRepository.getAllMeals()
            .filter { it.description?.contains("seafood", ignoreCase = true) == true }
            .sortedByDescending { it.nutrition.protein }
            .takeIf { it.isNotEmpty() }
            ?: throw NoSuchElementException("No seafood meals found.")
    }

}