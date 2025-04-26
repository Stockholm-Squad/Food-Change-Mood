package org.example.logic.usecases

import model.Meal
import model.Nutrition
import org.example.logic.repository.MealsRepository
import org.example.model.FoodChangeMoodExceptions.LogicException.NoMealsForSoThinPeopleException

class GetMealForSoThinPeopleUseCase(
    private val mealRepository: MealsRepository,
) {
    private val seenMeal: MutableSet<Int> = mutableSetOf()
    private val minCalories = 700
    fun suggestRandomMealForSoThinPeople(): Result<Meal> {
        return mealRepository.getAllMeals().fold(
            onSuccess = { meal ->
                handleGetAllMealsSuccess(meal)
            },
            onFailure = { Result.failure(NoMealsForSoThinPeopleException()) }
        )

    }

    private fun Nutrition.hasMoreThanMinCalories(): Boolean {
        return (this.calories != null) && (this.calories > minCalories)
    }

    private fun handleGetAllMealsSuccess(meals: List<Meal>): Result<Meal> {
        return Result.success(meals.filter { meal ->
            meal.nutrition?.hasMoreThanMinCalories() == true &&
                    meal.id !in seenMeal
        }
            .randomOrNull()
            ?.also { meal ->
                seenMeal.add(meal.id)
            } ?: return Result.failure(NoMealsForSoThinPeopleException())

        )
    }
}

