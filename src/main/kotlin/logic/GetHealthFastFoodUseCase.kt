package logic

import model.Meal
import org.example.logic.MealsRepository
import java.util.Collections.emptyList

//TODO rename the use case GetHealthyFastFoodUseCase
//TODO handle logic here
class GetHealthFastFoodUseCase(
    private val mealRepository: MealsRepository
) {
    fun getHealthyFastFood(): List<Meal> {
        val allMeals = mealRepository.getAllMeals()
        //TODO return the data direct instead of making variables
        val (avrCalories, avrFat, avrCarbo) = calculateAverageNutrition(allMeals)
        return allMeals.filter { it.isHealthyFastFood(avrCalories, avrFat, avrCarbo) }.take(10)
    }


    fun calculateAverageNutrition(meals: List<Meal>): Triple<Double, Double, Double> {
        val averageCalories = meals.map { it.nutrition.calories }.average()
        val averageTotalFat = meals.map { it.nutrition.totalFat }.average()
        val averageCarbohydrates = meals.map { it.nutrition.carbohydrates }.average()
        return Triple(averageCalories, averageTotalFat, averageCarbohydrates)
    }

    fun Meal.isHealthyFastFood(
        avrCalories: Double, avrFat: Double, avrCarbo: Double
    ): Boolean {
        return minutes?.let { it <= 15 } == true
                && (nutrition.calories?.let { it < avrCalories }) == true
                && (nutrition.totalFat?.let { it < avrFat }) == true
                && (nutrition.carbohydrates?.let { it < avrCarbo }) == true
    }
}