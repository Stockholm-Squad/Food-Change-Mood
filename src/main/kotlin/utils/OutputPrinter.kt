package org.example.utils

import model.Meal

interface OutputPrinter {
    fun printLine(value: String?)
    fun printMeals(allMeals: List<Meal>?)
    fun printMeal(meal: Meal?)
}