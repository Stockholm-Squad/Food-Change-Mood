package org.example.logic.usecases

import model.Meal
import org.example.logic.model.Results
import org.example.logic.repository.MealsRepository
import org.example.utils.Constants


class GetMealByNameUseCase(
    private val mealsRepository: MealsRepository,
    private val searchingByKmpUseCase: SearchingByKmpUseCase
) {

    fun getMealByName(query: String?): Results<List<Meal>> {
        if (query.isNullOrEmpty()) {
            return Results.Fail(IllegalArgumentException(Constants.SEARCH_QUERY_CAN_NOT_BE_EMPTY))
        }

        return when (val results = mealsRepository.getAllMeals()) {
            is Results.Success -> results.model
                .filter { meal -> searchingByKmpUseCase.searchByKmp(meal.name, query) }
                .takeIf { it.isNotEmpty() }
                ?.let { Results.Success(it) }
                ?: Results.Fail(Throwable(Constants.NO_MEALS_FOUND_MATCHING))

            is Results.Fail -> Results.Fail(results.exception)
        }
    }
}

