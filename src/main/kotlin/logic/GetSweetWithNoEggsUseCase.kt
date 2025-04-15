package org.example.logic

import model.Meal

class GetSweetWithNoEggsUseCase(private val mealRepository: MealRepository) {
    
    fun getDessertWithNoEggs(): List<Meal> {
        val meals = mealRepository.getAllMeals()
        val dessertsWithNoEggs = mutableListOf<Meal>()

        meals.forEach {
            if (it.tags!!.contains("dessert") && !it.ingredients!!.contains("egg"))
                dessertsWithNoEggs.add(it)
        }

        return dessertsWithNoEggs
    }
}