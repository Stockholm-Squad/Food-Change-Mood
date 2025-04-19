package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository


class GetPotatoMealsUseCase(private val mealsRepository: MealsRepository) {

    /**
     * A use case to retrieve a specified number of random meals containing "potato" as an ingredient.
     *
     * @param count The number of random meals to retrieve.
     * @return A list of random meals containing "potato".
     */

    fun getRandomPotatoMeals(count: Int): List<Meal> {
        return mealsRepository.getAllMeals()
            .filter { meal ->
                meal.ingredients?.any { ingredient ->
                    ingredient.equals("potato", ignoreCase = true)
                } ?: false
            }
            .shuffled()
            .take(count)
    }
}