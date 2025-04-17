package org.example.utils

import model.Meal

fun viewMealInListDetails(mealsList: List<Meal>, mealId: Int) {
    val meal: Meal? = mealsList.find { meal ->
        meal.id == mealId
    }

    if (meal == null) {
        println("The meal with ID $mealId does not exist.")
        return
    }

    println(meal)
}