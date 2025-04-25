package org.example.logic.usecases

import model.Meal
import model.Nutrition
import org.example.logic.repository.MealsRepository

class GetMealForKetoDietUseCase(private val mealRepository: MealsRepository) {

    private val suggestedMeals = mutableSetOf<Int>()

    fun getKetoMeal(): Result<Meal?> {
        return mealRepository.getAllMeals().fold(
            onSuccess = { meals ->
                val meal = meals.asSequence()
                    .filter { meal ->
                        meal.nutrition?.let { isValidNutritionForKetoMeal(it) } == true
                    }
                    .filterNot { suggestedMeals.contains(it.id) }
                    .shuffled()
                    .firstOrNull()
                    ?.also { suggestedMeals.add(it.id) }

                Result.success(meal)
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }

    private fun isValidNutritionForKetoMeal(nutrition: Nutrition): Boolean =
        nutrition.carbohydrates != null &&
                nutrition.totalFat != null &&
                nutrition.protein != null &&
                nutrition.carbohydrates < 10 &&
                nutrition.totalFat > 15 &&
                nutrition.protein in 10f..30f
}
