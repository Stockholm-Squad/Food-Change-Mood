package org.example.logic.usecase

import model.Meal
import org.example.logic.repo.IMealRepository

class GetHealthyFastFoodUseCase(
    private val mealRepository: IMealRepository
) {
    fun getHealthyFastFood(): List<Meal> {
        val allMeals = mealRepository.getAllMeals()
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
        return preparationTime?.let { it <= 15 } == true
                && (nutrition.calories?.let { it < avrCalories }) == true
                && (nutrition.totalFat?.let { it < avrFat }) == true
                && (nutrition.carbohydrates?.let { it < avrCarbo }) == true
    }
}