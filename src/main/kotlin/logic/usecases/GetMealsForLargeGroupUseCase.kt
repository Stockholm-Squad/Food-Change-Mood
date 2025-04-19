package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository

class GetMealsForLargeGroupUseCase(
    private val mealsRepository: MealsRepository
) {

    fun getItalianMealsForLargeGroup(): List<Meal> {
        return mealsRepository.getAllMeals()
            .filter(::isItalianMealsForLargeGroup)
            .sortedBy { it.id }
    }

    private fun isItalianMealsForLargeGroup(meal: Meal): Boolean {
        val tags: List<String> = meal.tags ?: return false
        return tags.contains("for-large-groups")
                && tags.contains("italian")
    }
}