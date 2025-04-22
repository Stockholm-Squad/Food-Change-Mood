package org.example.logic.usecases

import model.Meal
import org.example.logic.model.Results
import org.example.logic.repository.MealsRepository

class GetItalianMealsForLargeGroupUseCase(
    private val mealsRepository: MealsRepository
) {
    
    fun getMeals(): Results<List<Meal>> {
        return when (val allMeals = mealsRepository.getAllMeals()) {
            is Results.Success -> Results.Success(allMeals.model.filter(::isItalianMealsForLargeGroup)
                .sortedBy { it.id })

            is Results.Fail -> Results.Fail(allMeals.exception)

        }
    }

    private fun isItalianMealsForLargeGroup(meal: Meal): Boolean {
        val tags: List<String> = meal.tags ?: return false
        return tags.contains("for-large-groups")
                && tags.contains("italian")
    }
}