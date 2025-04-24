package org.example.logic.usecases

import model.Meal
import model.Nutrition
import org.example.logic.repository.MealsRepository

class GetMealsForSoThinProblemUseCase(
    private val mealRepository: MealsRepository,
) {
    fun suggestRandomMealForSoThinPeople(): Result<Meal> {
        return mealRepository.getAllMeals().fold(
            onSuccess = { meals ->
                Result.success(
                    meals.takeIf { meals -> meals.isNotEmpty() }
                        ?.filter { meal ->
                            meal.id !in seenMeal &&
                                    meal.nutrition?.hasMoreThanMinCalories() == true
                        }
                        ?.randomOrNull()
                        ?.also { meal ->
                            seenMeal.add(meal.id)
                        }
                        ?: throw IllegalStateException("Sorry, No meal found with more than 700 calories"))
            },
            onFailure = { exception -> Result.failure(exception) }
        )

    }

    private fun Nutrition.hasMoreThanMinCalories(): Boolean {
        return (this.calories != null) && (this.calories > MIN_CALORIES)
    }

    companion object {
        private const val MIN_CALORIES = 700
        private val seenMeal: MutableSet<Int> = mutableSetOf()

    }
}