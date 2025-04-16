package logic

import model.Meal
import org.example.logic.MealsRepository
import java.util.Collections.emptyList

class GetHealthFastFoodUseCase(
    private val mealRepository: MealsRepository
) {
    fun getHealthyFastFood():List<Meal>{
        return emptyList()
    }
}