package org.example.utils

import model.Meal

fun viewMealInListDetails(mealsList: List<Meal>, mealId: Int) {
    //TODO check this to move it to the use case or make it extension
    val meal: Meal? = mealsList.find { meal ->
        meal.id == mealId
    }

    if (meal == null) {
        println("The meal with ID $mealId does not exist.")
        return
    }

    println(meal)
}