package org.example.logic

import model.Nutrition

//TODO rename the useCase
class SoThinProblem(
    private val mealRepository: MealsRepository,
) {
    //TODO handle the return type to be Meal
    fun suggestMealToSoThinProblem(): Triple<String?, String?, Pair<Int?, Float>> {
        return mealRepository.getAllMeals()
            .takeIf { meals ->
                meals.isNotEmpty()
            }
            ?.filter { meal ->
                meal.nutrition.hasMoreThanMinCalories()
            }
            ?.map { suggestMeal ->
                Triple(
                    suggestMeal.name,
                    suggestMeal.description,
                    Pair(suggestMeal.minutes, suggestMeal.nutrition.calories)
                )
            }
            ?.random() ?: throw IllegalStateException("Sorry, No meal found with more than 700 calories")
    }

    private fun Nutrition.hasMoreThanMinCalories(): Boolean {
        return this.calories > MIN_CALORIES
    }

    companion object {
        private const val MIN_CALORIES = 700
    }
}