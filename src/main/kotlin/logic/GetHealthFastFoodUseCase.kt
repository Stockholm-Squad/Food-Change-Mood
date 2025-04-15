package logic

import model.Meal
import org.example.logic.IMealsRepository
import java.util.Collections.emptyList

class GetHealthFastFoodUseCase(
    private val mealRepository: IMealsRepository
) {
    fun getHealthyFastFood(): List<Meal> {
        val allMeals = mealRepository.getAllMeals()
        allMeals.filter { it.preparationTime ?: Int.MAX_VALUE <= 15 }
            .sortedWith(
                compareBy<Meal> { it.nutrition.totalFat }
                    .thenBy { it.nutrition.saturatedFat }
                    .thenBy { it.nutrition.carbohydrates }
            )
        return allMeals
    }
}