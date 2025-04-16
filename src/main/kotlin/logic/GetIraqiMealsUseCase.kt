package logic
import model.Meal
import org.example.logic.MealRepository

class GetIraqiMealsUseCase(
    private val mealRepository: MealRepository
) {
    fun getIraqiMales(): List<Triple<String, Int, String?>> {
        return mealRepository.getAllMeals()
            .filter(::onlyNotEmptyData)
            .filter {meal->
                meal.description!!.contains("iraq",ignoreCase = true) || meal.tags!!.contains("iraqi") }
            .map { iraqiMeal ->
                Triple(iraqiMeal.name,iraqiMeal.minutes,iraqiMeal.description)}
    }
    private fun onlyNotEmptyData(input:Meal):Boolean{
        return input.description.toString().isNotBlank() || input.tags?.size  != 0
    }
}
