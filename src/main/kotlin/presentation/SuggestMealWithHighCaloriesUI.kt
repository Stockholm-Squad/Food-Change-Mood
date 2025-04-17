package org.example.presentation

import org.example.logic.SoThinProblem


class SuggestMealWithHighCaloriesUI(
    private val soThinProblem: SoThinProblem,
) {
    fun getMaleWithHighCalorie() {
        println("-------------------------------------------------------------------------------------------------")
        println("🔥 Feeling too thin? This meal above 700 calories!")
        val suggestMeal = soThinProblem.suggestMealToSoThinProblem()
        println("------------------------------------------------------------------------------------------------")
        println("name: " + suggestMeal.first)
        println("description: " + suggestMeal.second)
        println("time: " + suggestMeal.third.first + " minute")
        println("calories: " + suggestMeal.third.second)
        println("--------------------------------------------------------------------------------------------------")
        println("Do you like it? (yes/no) 😊")
        when(readlnOrNull()?.trim()?.lowercase()) {
            "no" -> return getMaleWithHighCalorie()
            "yes" -> println("Great! Enjoy 😋")
            else -> throw Exception("Invalid input! Expected yes or no.")
        }
    }
}