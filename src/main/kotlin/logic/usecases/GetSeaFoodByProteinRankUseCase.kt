package org.example.logic.usecases

import model.Meal
import org.example.logic.model.Results
import org.example.logic.repository.MealsRepository

class GetSeaFoodByProteinRankUseCase(private val mealsRepository: MealsRepository) {
    fun getSeaFoodByProteinRank(): Results<List<Meal>> {
        return when (val results = mealsRepository.getAllMeals()) {
            is Results.Success -> results.model.filter {
                it.description?.contains(
                    "seafood",
                    ignoreCase = true
                ) == true
            }
                .sortedByDescending { it.nutrition?.protein }
                .takeIf { it.isNotEmpty() }?.let {
                    Results.Success(it)
                } ?: Results.Fail(NoSuchElementException("No seafood meals found."))

            is Results.Fail -> Results.Fail(results.exception)
        }
    }


}