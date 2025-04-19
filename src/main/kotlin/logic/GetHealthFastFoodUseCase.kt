package logic

import model.Meal
import org.example.logic.MealsRepository


class GetHealthyFastFoodUseCase(
    private val mealRepository: MealsRepository
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


