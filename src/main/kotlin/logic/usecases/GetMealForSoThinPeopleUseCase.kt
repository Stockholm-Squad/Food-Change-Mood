package org.example.logic.usecases

import model.Meal
import model.Nutrition
import org.example.logic.repository.MealsRepository
import org.example.utils.Constants

class GetMealForSoThinPeopleUseCase(
    private val mealRepository: MealsRepository,
) {
    fun suggestRandomMealForSoThinPeople(): Result<Meal> {
        return mealRepository.getAllMeals().fold(
            onSuccess = { meals ->
                Result.success(
                    meals.filter { meal ->
                        meal.id !in seenMeal &&
                                meal.nutrition?.hasMoreThanMinCalories() == true
                    }
                        .randomOrNull()
                        ?.also { meal ->
                            seenMeal.add(meal.id)
                        }?: return Result.failure(Throwable(Constants.NO_MEAL_FOR_SO_THIN_PEOPLE)))
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