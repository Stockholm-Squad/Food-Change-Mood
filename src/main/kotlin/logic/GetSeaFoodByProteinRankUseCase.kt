package org.example.logic

import model.Meal

class GetSeaFoodByProteinRankUseCase(private val mealsRepository: MealsRepository) {
    fun getSeaFoodByProteinRank(): List<Meal> {
        return mealsRepository.getAllMeals()
            .filter { it.description?.contains("seafood", ignoreCase = true) == true }
            .sortedByDescending { it.nutrition.protein }
            .takeIf { it.isNotEmpty() }
            ?: throw NoSuchElementException("No seafood meals found.")
    }

}