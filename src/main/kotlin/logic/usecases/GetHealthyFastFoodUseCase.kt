package org.example.logic.usecases

import model.Meal
import org.example.logic.model.Results
import org.example.logic.repository.MealsRepository


class GetHealthyFastFoodUseCase(
    private val mealsRepository: MealsRepository
) {
    fun getHealthyFastFood(): Results<List<Meal>> {
        return when (val meals = mealsRepository.getAllMeals()) {
            is Results.Success -> {
                meals.model.filter { it.minutes != null && it.minutes <= 15 }
                    .sortedWith(
                        compareBy<Meal> { it.nutrition?.totalFat }
                            .thenBy { it.nutrition?.saturatedFat }
                            .thenBy { it.nutrition?.carbohydrates }
                    )
                Results.Success(meals.model)
            }

            is Results.Fail -> Results.Fail(meals.exception)
        }
    }
}


