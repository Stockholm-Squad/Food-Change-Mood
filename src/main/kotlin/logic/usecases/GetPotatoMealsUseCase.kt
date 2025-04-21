package org.example.logic.usecases

import model.Meal
import org.example.logic.model.FoodChangeModeResults
import org.example.logic.repository.MealsRepository


class GetPotatoMealsUseCase(private val mealsRepository: MealsRepository) {
    fun getRandomPotatoMeals(count: Int): FoodChangeModeResults<List<Meal>> {
        return when (val results = mealsRepository.getAllMeals()) {
            is FoodChangeModeResults.Success -> results.model
                .filter { meal ->
                    meal.ingredients?.any { ingredient ->
                        ingredient.equals("potato", ignoreCase = true)
                    } == true
                }
                .shuffled()
                .take(count)
                .takeIf { it.isNotEmpty() }
                ?.let { FoodChangeModeResults.Success(it) }
                ?: FoodChangeModeResults.Fail(NoSuchElementException("No potato meals found."))

            is FoodChangeModeResults.Fail -> FoodChangeModeResults.Fail(results.exception)
        }
    }
}
