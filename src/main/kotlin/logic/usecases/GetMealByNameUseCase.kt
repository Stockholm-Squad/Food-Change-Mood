package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository


class GetMealByNameUseCase(
    private val mealsRepository: MealsRepository,
    private val searchingByKmpUseCase: SearchingByKmpUseCase
) {

    /**
     * Searches for meals whose name contains the given query (case-insensitive).
     *
     * @param query The name or part of the name to search for.
     * @return A list of meals matching the query.
     */
    fun getMealByName(query: String?): List<Meal> {
        if (query.isNullOrEmpty()) return emptyList()

        return mealsRepository.getAllMeals().filter { meal ->
            searchingByKmpUseCase.searchByKmp(meal.name, query)
        }
    }
}

