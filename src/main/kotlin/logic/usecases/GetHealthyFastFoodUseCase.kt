package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.utils.Constants


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
                Result.failure(Throwable("${Constants.ERROR_FETCHING_MEALS} ${error.message}"))
            }

        )

    }

    fun getFilteredMeals(allMeals: List<Meal>): List<Meal >{
        return allMeals .filter { it.minutes != null && it.minutes <= 15 }
            .filter { it.nutrition != null }
            .sortedWith(
                compareBy<Meal> { it.nutrition?.totalFat }
                    .thenBy { it.nutrition?.saturatedFat }
                    .thenBy { it.nutrition?.carbohydrates }
            )
    }
}


