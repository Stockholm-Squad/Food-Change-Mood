package org.example.utils

import model.Meal
import org.example.input_output.output.OutputPrinter
import java.util.Date
import java.text.SimpleDateFormat


fun Date.print(): String{
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    return dateFormat.format(this)
}

fun List<Meal>.viewMealInListDetails(mealId: Int, printer: OutputPrinter) {
    val meal: Meal? = this.find { meal ->
        meal.id == mealId
    }

    if (meal == null) {
        printer.printLine("The meal with ID $mealId does not exist.")
        return
    }

    printer.printMeal(meal)
}