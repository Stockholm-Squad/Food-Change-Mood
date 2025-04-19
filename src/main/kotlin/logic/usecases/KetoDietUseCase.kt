package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository

class KetoDietUseCase(private val mealRepository: MealsRepository) {

    private val suggestedMeals = mutableSetOf<Int>()

    //TODO handle renaming of this function
    //TODO handle it to be functional programming
    fun getNextKetoMeal(): Meal? {
        val allMeals = mealRepository.getAllMeals()

        val ketoMeals = allMeals.filter { meal ->
            meal.nutrition?.let { nutrition ->
                if (nutrition.carbohydrates != null && nutrition.totalFat != null && nutrition.protein != null) {
                    nutrition.carbohydrates < 10 &&
                            nutrition.totalFat > 15 &&
                            nutrition.protein in 10f..30f
                } else {
                    false
                }
            } == true
        }.filterNot { meal ->
            suggestedMeals.contains(meal.id)
        }
        
        if (ketoMeals.isEmpty()) {
            return null
        }

        val nextMeal = ketoMeals.random()
        suggestedMeals.add(nextMeal.id)

        return nextMeal
    }
}
