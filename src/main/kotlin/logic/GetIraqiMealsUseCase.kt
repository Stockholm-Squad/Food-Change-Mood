package logic

import model.Meal
import org.example.logic.MealsRepository

class GetIraqiMealsUseCase(
    private val mealRepository: MealsRepository,
) {
    fun getIraqiMales(): List<Triple<String?, Int?, String?>> {
        return mealRepository.getAllMeals()
            .takeIf { meals ->
                meals.isNotEmpty()
            }
            ?.filter(::onlyNotEmptyData)
            ?.filter { meal ->
                meal.description?.contains("iraq", ignoreCase = true) == true || meal.tags?.contains("iraqi") == true
            }
            ?.map { iraqiMeal ->
                Triple(iraqiMeal.name, iraqiMeal.minutes, iraqiMeal.description)
            } ?: throw IllegalStateException("Sorry, we don't have any iraqi meals available")

    }

    private fun onlyNotEmptyData(input: Meal): Boolean {
        return input.description.toString().isNotBlank() || input.tags?.size != 0
    }


}