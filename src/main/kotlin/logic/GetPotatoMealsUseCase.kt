package org.example.logic

import model.Meal

//TODO handle it to be separated or handle the name to be GetRandomPotatoMealsUseCase and make the 2 functions to be one

class GetPotatoMealsUseCase(private val mealsRepository: MealsRepository) {

    /**
     * Filters all meals to return only those that contain "potato" as an ingredient.
     *
     * This function checks each meal's ingredients list and includes it in the result
     * if any ingredient matches "potato", ignoring case.
     *
     * @return A list of meals that contain "potato".
     */

    private fun getPotatoMeals(): List<Meal> {
        return mealsRepository.getAllMeals().filter { meal ->
            meal.ingredients?.any { ingredient ->
                ingredient.equals("potato", ignoreCase = true)
            } ?: false
        }
    }

    /**
     * Retrieves a specified number of random meals containing "potato".
     *
     * @param count The number of random meals to retrieve.
     * @return A list of random meals containing "potato".
     */
    fun getRandomPotatoMeals(count: Int): List<Meal> {

        return getPotatoMeals().shuffled().take(count)
    }

}