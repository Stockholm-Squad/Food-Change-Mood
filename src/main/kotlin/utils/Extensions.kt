package org.example.utils

import model.Meal
import java.util.Date
import java.text.SimpleDateFormat


fun Date.print(): String{
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    return dateFormat.format(this)
}

fun List<Meal>.viewMealInListDetails(mealId: Int) {
    val meal: Meal? = this.find { meal ->
        meal.id == mealId
    }

    if (meal == null) {
        println("The meal with ID $mealId does not exist.")
        return
    }

    println(meal)
}