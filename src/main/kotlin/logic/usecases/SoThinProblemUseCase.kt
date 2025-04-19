package org.example.logic.usecases

import model.Meal
import model.Nutrition
import org.example.logic.repository.MealsRepository

class SoThinProblemUseCase(
    private val mealRepository: MealsRepository,
) {
    fun suggestRandomMealForSoThinPeople(): Meal {
        return mealRepository.getAllMeals()
            .takeIf { meals -> meals.isNotEmpty() }
            ?.filter { meal ->
                meal.id !in seenMeal &&
                        meal.nutrition?.hasMoreThanMinCalories() == true
            }
            ?.randomOrNull()
            ?.also { meal ->
                seenMeal.add(meal.id)
            }
            ?: throw IllegalStateException("Sorry, No meal found with more than 700 calories")
    }

    private fun Nutrition.hasMoreThanMinCalories(): Boolean {
        return (this.calories != null) && (this.calories > MIN_CALORIES)
    }

    companion object {
        private const val MIN_CALORIES = 700
        private val seenMeal: MutableSet<Int> = mutableSetOf()

    }
}