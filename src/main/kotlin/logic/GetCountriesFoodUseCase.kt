package org.example.logic

import model.Meal

class GetCountriesFoodUseCase(private val mealRepository: MealRepository) {
    fun getRandomMeals(countryName: String): List<Meal> {
        val meals = mealRepository.getAllMeals()
        val countryMeals = mutableListOf<Meal>()
        val only20Meals = mutableListOf<Meal>()

        meals.forEach {
            if (it.name!!.contains(countryName) || it.tags!!.contains(countryName))
                countryMeals.add(it)
        }

        for (i in 0..20) {
            val randomIndex = (0..countryMeals.size).random()
            only20Meals.add(countryMeals[randomIndex])
        }
        return only20Meals

    }
}