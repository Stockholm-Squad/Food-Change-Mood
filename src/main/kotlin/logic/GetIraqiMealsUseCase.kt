package logic
import model.Meal
import org.example.logic.MealRepository

class GetIraqiMealsUseCase(
    private val mealRepository: MealRepository
) {
    fun getIraqiMales(): List<Triple<String, Int, String?>> {
        return mealRepository.getAllMeals()
            .filter(::onlyNotNullData)
            .filter {meal->
                meal.description!!.contains("iraq",ignoreCase = true) || meal.tags!!.contains("iraqi") }
            .take(MAX_NUMBER_OF_MEALS_TO_TAKE)
            .map { iraqiMeal ->
                Triple(iraqiMeal.name,iraqiMeal.preparationTime,iraqiMeal.description)}

    }
    private fun onlyNotNullData(input:Meal):Boolean{
        return input.description != null || input.tags != null
    }

    companion object{
        private const val MAX_NUMBER_OF_MEALS_TO_TAKE = 50
    }
}
