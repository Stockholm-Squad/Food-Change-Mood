package org.example.logic.usecase

import model.Meal
import org.example.logic.repo.IMealsRepository

class GetHealthFastFoodUseCase(
  private val mealRepository: IMealsRepository
) {
    fun getHealthyFastFood(): List<Meal> {
        val allMeals =mealRepository.getAllMeals()
      return  allMeals.filter { it.preparationTime!! <= 15 }
            .sortedWith(
                compareBy<Meal> { it.nutrition.totalFat }
                    .thenBy { it.nutrition.saturatedFat }
                    .thenBy { it.nutrition.carbohydrates }
            )





    }
}