package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.utils.Constants

class GetMealByNameUseCase(
    private val mealsRepository: MealsRepository,
    private val searchingByKmpUseCase: SearchingByKmpUseCase
) {

    fun getMealByName(query: String?): Result<List<Meal>> {
        if (query.isNullOrEmpty()) {
            return Result.failure(IllegalArgumentException(Constants.SEARCH_QUERY_CAN_NOT_BE_EMPTY))
        }

        return mealsRepository.getAllMeals().fold(
            onSuccess = { allMeals ->
                val filteredMeals = allMeals.filter {
                    searchingByKmpUseCase.searchByKmp(it.name, query)
                }

                if (filteredMeals.isNotEmpty()) {
                    Result.success(filteredMeals)
                } else {
                    Result.failure(NoSuchElementException("${Constants.NO_MEALS_FOUND_MATCHING} '$query'"))
                }
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}
