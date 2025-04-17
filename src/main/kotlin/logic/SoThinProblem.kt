package org.example.logic
import model.Nutrition
class SoThinProblem(
    private val mealRepository: MealsRepository
) {
    fun suggestMealToSoThinProblem(): Triple<String?, String?, Pair<Int?, Float>> {
        return mealRepository.getAllMeals()
            .filter{it.nutrition.onlyMoreThan700Calories()}
            .map {Triple(it.name,it.description,Pair(it.minutes,it.nutrition.calories))}
            .random()

    }
    private fun Nutrition.onlyMoreThan700Calories(): Boolean {
        return this.calories > CALORIES
    }

    companion object{
        private const val CALORIES = 700
    }
}