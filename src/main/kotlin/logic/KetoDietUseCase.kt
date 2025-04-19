package org.example.logic

import model.Meal

class KetoDietUseCase(private val mealRepository: MealsRepository) {

    private val suggestedMeals = mutableSetOf<Int>()

    //TODO handle renaming of this function
    //TODO handle it to be functional programming
    fun getNextKetoMeal(): Meal? {
        val allMeals = mealRepository.getAllMeals()

        val ketoMeals = allMeals.filter { meal ->
            meal.nutrition.carbohydrates < 10 && meal.nutrition.totalFat > 15 && meal.nutrition.protein in 10f..30f
        }.filterNot { meal ->
            suggestedMeals.contains(meal.id)
        }

        if (ketoMeals.isEmpty()) {
            return null
        }

        val nextMeal = ketoMeals.random()
        suggestedMeals.add(nextMeal.id)

        return nextMeal
    }
}
