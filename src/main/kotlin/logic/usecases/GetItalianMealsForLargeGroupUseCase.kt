package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository

class GetItalianMealsForLargeGroupUseCase(
    private val mealsRepository: MealsRepository
) {

    fun getMeals(): Result<List<Meal>> {
        return mealsRepository.getAllMeals().fold(
            onSuccess = { meals ->
                Result.success(filterItalianMealsForLargeGroup(meals))
            },
            onFailure = { exception -> Result.failure(exception) }
        )
    }

    private fun filterItalianMealsForLargeGroup(meals: List<Meal>): List<Meal> {
        return meals.filter(::isItalianMealsForLargeGroup)
            .sortedBy { it.id }
    }

    private fun isItalianMealsForLargeGroup(meal: Meal): Boolean {
        val tags: List<String> = meal.tags ?: return false
        return tags.contains("for-large-groups")
                && tags.contains("italian")
    }
}