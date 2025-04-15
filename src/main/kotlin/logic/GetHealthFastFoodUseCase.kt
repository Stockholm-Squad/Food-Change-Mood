package logic

import org.example.logic.MealRepository
import model.Meal
import java.util.Collections.emptyList

class GetHealthFastFoodUseCase(
    private val mealRepository: MealRepository
) {
    fun getHealthyFastFood():List<Meal>{
        return emptyList()
    }
}