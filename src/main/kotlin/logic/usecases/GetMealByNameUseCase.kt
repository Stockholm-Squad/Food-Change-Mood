package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.utils.Constants

class GetMealByNameUseCase(
    private val mealsRepository: MealsRepository,
    private val searchingByKmpUseCase: SearchingByKmpUseCase
) {

    fun getMealByName(query: String?): Result<List<Meal>> {
        return query?.takeIf { it.isNotBlank() }
            ?.let { safeQuery ->
                mealsRepository.getAllMeals().fold(
                    onSuccess = { handleSuccess(it, safeQuery) },
                    onFailure = ::handleFailure
                )
            }
            ?: handleFailure(Throwable(Constants.SEARCH_QUERY_CAN_NOT_BE_EMPTY))
    }

    private fun handleSuccess(meals: List<Meal>, query: String): Result<List<Meal>> =
        meals
            .filter { searchingByKmpUseCase.searchByKmp(it.name, query) }
            .takeIf { it.isNotEmpty() }
            ?.let { Result.success(it) }
            ?: handleFailure(Throwable(Constants.NO_MEALS_FOUND_MATCHING))

    private fun handleFailure(error: Throwable): Result<List<Meal>> =
        Result.failure(Throwable("${Constants.ERROR_FETCHING_MEALS}${error.message}"))

}
