package logic

import org.example.logic.IMealsRepository
import model.Meal
import java.util.Collections.emptyList

class GetHealthFastFoodUseCase(
    private val mealRepository: IMealsRepository
) {
    fun getHealthyFastFood():List<Meal>{
        return emptyList()
    }
}