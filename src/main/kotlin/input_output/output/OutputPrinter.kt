package org.example.input_output.output

import model.Meal

interface OutputPrinter {
    fun printLine(value: String?)
    fun printMeals(allMeals: List<Meal>?)
    fun printMeal(meal: Meal?)
}