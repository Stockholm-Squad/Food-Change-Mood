package org.example.logic.usecases

import model.Meal
import org.example.logic.model.FoodChangeModeResults
import org.example.logic.repository.MealsRepository

class GetSeaFoodByProteinRankUseCase(private val mealsRepository: MealsRepository) {
    fun getSeaFoodByProteinRank(): FoodChangeModeResults<List<Meal>> {
        return when (val results = mealsRepository.getAllMeals()) {
            is FoodChangeModeResults.Success -> results.model.filter {
                it.description?.contains(
                    "seafood",
                    ignoreCase = true
                ) == true
            }
                .sortedByDescending { it.nutrition?.protein }
                .takeIf { it.isNotEmpty() }?.let {
                    FoodChangeModeResults.Success(it)
                } ?: FoodChangeModeResults.Fail(NoSuchElementException("No seafood meals found."))

            is FoodChangeModeResults.Fail -> FoodChangeModeResults.Fail(results.exception)
        }
    }


}