package org.example.logic.usecases

import model.Meal
import org.example.logic.model.FoodChangeModeResults
import org.example.logic.repository.MealsRepository


class GetMealByNameUseCase(
    private val mealsRepository: MealsRepository,
    private val searchingByKmpUseCase: SearchingByKmpUseCase
) {

    fun getMealByName(query: String?): FoodChangeModeResults<List<Meal>> {
        if (query.isNullOrEmpty()) {
            return FoodChangeModeResults.Fail(IllegalArgumentException("Search query cannot be empty."))
        }

        return when (val results = mealsRepository.getAllMeals()) {
            is FoodChangeModeResults.Success -> results.model
                .filter { meal -> searchingByKmpUseCase.searchByKmp(meal.name, query) }
                .takeIf { it.isNotEmpty() }
                ?.let { FoodChangeModeResults.Success(it) }
                ?: FoodChangeModeResults.Fail(NoSuchElementException("No meals found matching '$query'."))

            is FoodChangeModeResults.Fail -> FoodChangeModeResults.Fail(results.exception)
        }
    }
}

