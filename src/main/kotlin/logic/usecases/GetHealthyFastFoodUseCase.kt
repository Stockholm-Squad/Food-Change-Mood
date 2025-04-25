package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.model.FoodChangeMoodExceptions


class GetHealthyFastFoodUseCase(
    private val mealsRepository: MealsRepository
) {
    fun getHealthyFastFood(): Result<List<Meal>> {

        return mealsRepository.getAllMeals().fold(
            onSuccess = { allMeals ->
                Result.success(
                    getFilteredMeals(allMeals)
                )
            },
            onFailure = { error ->
                Result.failure(error)
            }

        )

    }

    private fun getFilteredMeals(allMeals: List<Meal>): List<Meal> {
        return allMeals
            .filter { meal ->
                meal.minutes != null &&
                        meal.minutes <= 15 &&
                        meal.nutrition != null &&
                        meal.tags?.contains("healthy") == true
            }
            .sortedWith(
                compareBy<Meal> { it.nutrition!!.totalFat }
                    .thenBy { it.nutrition!!.saturatedFat }
                    .thenBy { it.nutrition!!.carbohydrates }
            )
            .takeIf { it.isNotEmpty() }
            ?: throw FoodChangeMoodExceptions.LogicException.NoMealsFound()
    }
}


