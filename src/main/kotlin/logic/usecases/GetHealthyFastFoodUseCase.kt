package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository


class GetHealthyFastFoodUseCase(
    private val mealsRepository: MealsRepository
) {
     fun getHealthyFastFood(): List<Meal> {
        return mealsRepository.getAllMeals().filter { it.minutes!=null&&it.minutes<=15 }
            .sortedWith(
                compareBy<Meal>{it.nutrition.totalFat}
                    .thenBy { it.nutrition.saturatedFat }
                    .thenBy { it.nutrition.carbohydrates }
            )
    }
}


