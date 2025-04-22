package org.example.logic.usecases

import model.Meal
import org.example.logic.model.Results
import org.example.logic.repository.MealsRepository
import org.example.utils.Constants


class GetPotatoMealsUseCase(private val mealsRepository: MealsRepository) {
    fun getRandomPotatoMeals(count: Int): Results<List<Meal>> {
        return when (val results = mealsRepository.getAllMeals()) {
            is Results.Success -> results.model
                .filter { meal ->
                    meal.ingredients?.any { ingredient ->
                        ingredient.equals(Constants.POTATO, ignoreCase = true)
                    } == true
                }
                .shuffled()
                .take(count)
                .takeIf { it.isNotEmpty() }
                ?.let { Results.Success(it) }
                ?: Results.Fail(NoSuchElementException(Constants.NO_MEALS_FOR_POTATO))

            is Results.Fail -> Results.Fail(results.exception)
        }
    }
}
