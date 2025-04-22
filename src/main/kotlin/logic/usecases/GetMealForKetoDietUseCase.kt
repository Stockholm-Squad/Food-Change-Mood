package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository

class GetMealForKetoDietUseCase(private val mealRepository: MealsRepository) {

    private val suggestedMeals = mutableSetOf<Int>()

    fun getKetoMeal(): Meal? =
        mealRepository.getAllMeals().fold(
            onSuccess = { meals ->
                meals.asSequence()
                .filter { meal ->
                    meal.nutrition?.let { nutrition ->
                        if (nutrition.carbohydrates != null && nutrition.totalFat != null && nutrition.protein != null) {
                            nutrition.carbohydrates < 10 &&
                                    nutrition.totalFat > 15 &&
                                    nutrition.protein in 10f..30f
                        } else {
                            false
                        }
                    } == true
                }
                .filterNot { suggestedMeals.contains(it.id) }
                .shuffled()
                .firstOrNull()
                ?.also { suggestedMeals.add(it.id) }
            },
            onFailure = {
                throw it
            }
        )

}
