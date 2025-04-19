package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository

class KetoDietUseCase(private val mealRepository: MealsRepository) {

    private val suggestedMeals = mutableSetOf<Int>()

    fun getKetoMeal(): Meal? {
        return mealRepository.getAllMeals()
            .asSequence()
            .filter { it.nutrition.carbohydrates < 10 && it.nutrition.totalFat > 15 && it.nutrition.protein in 10f..30f }
            .filterNot { suggestedMeals.contains(it.id) }
            .shuffled()
            .firstOrNull()
            ?.also { suggestedMeals.add(it.id) }
    }
}
