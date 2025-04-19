package org.example.logic

import model.Meal

//TODO rename it to GetMealsForCountryUseCase for example

class GetCountriesFoodUseCase(private val mealRepository: MealsRepository) {
    //TODO rename the function to be like getRandomMealsByCountry
    fun getRandomMeals(countryName: String): List<Meal> {
        return try {
            val meals = mealRepository.getAllMeals()
//TODO handle nullable here and separate the filter code in another function
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

