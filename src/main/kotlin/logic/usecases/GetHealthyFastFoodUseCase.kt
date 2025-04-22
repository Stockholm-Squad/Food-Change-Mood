package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository


class GetHealthyFastFoodUseCase(
    private val mealsRepository: MealsRepository
) {
    fun getHealthyFastFood(): Result<List<Meal>> {

        return mealsRepository.getAllMeals().fold(
            onSuccess = { allMeals ->
                Result.success(
                    allMeals.filter { meal ->
                        meal.minutes != null && meal.minutes <= 15
                    }
                        .sortedWith(
                            compareBy<Meal> { it.nutrition?.totalFat }
                                .thenBy { it.nutrition?.saturatedFat }
                                .thenBy { it.nutrition?.carbohydrates }
                        )
                )
            },
            onFailure = { error ->
                Result.failure(error)
            }

        )

    }
}


