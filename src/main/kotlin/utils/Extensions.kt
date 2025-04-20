package org.example.utils

import model.Meal
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

fun getDateFromString(stringDate: String): Date? {
    //parse date with this format 2005-02-25
    return try {
        Date.from(LocalDate.parse(stringDate).atStartOfDay(ZoneId.systemDefault()).toInstant())
    } catch (e: Exception) {
        return null
    }

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