package org.example.logic

import model.Meal


class GetCountriesFoodUseCase(private val mealRepository: MealsRepository) {
    fun getRandomMeals(countryName: String): List<Meal> {
        return try {
            val meals = mealRepository.getAllMeals()

            val countryMeals = meals.filter {
                it.name!!.contains(countryName, ignoreCase = true)
                        || it.tags!!.contains(countryName)
            }

            if (countryMeals.isEmpty()) {
                throw NoSuchElementException("No meals found for country: $countryName")
            }

            countryMeals.shuffled().take(20)

        } catch (e: Exception) {
            emptyList()
        }
    }

}

