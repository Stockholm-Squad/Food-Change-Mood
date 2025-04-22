package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.utils.Constants

class GetMealByNameUseCase(
    private val mealsRepository: MealsRepository,
    private val searchingByKmpUseCase: SearchingByKmpUseCase
) {

    fun getMealByName(query: String?): Result<List<Meal>> {
        query?.takeIf { it.isNotBlank() }
            ?: return Result.failure(Throwable(Constants.SEARCH_QUERY_CAN_NOT_BE_EMPTY))

        mealsRepository.getAllMeals().onSuccess { meals ->
            return Result.success(
                meals
                    .filter { searchingByKmpUseCase.searchByKmp(it.name, query) }
                    .takeIf { it.isNotEmpty() }
                    ?: return Result.failure(Throwable(Constants.NO_MEALS_FOUND_MATCHING))
            )
        }.onFailure { error ->
            return Result.failure(Throwable("${Constants.ERROR_FETCHING_MEALS} ${error.message}"))
        }

        return Result.failure(Throwable("${Constants.UNEXPECTED_ERROR} ${this::class.simpleName}"))
    }
}
