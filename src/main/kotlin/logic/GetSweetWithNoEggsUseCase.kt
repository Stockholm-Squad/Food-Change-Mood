package org.example.logic

import CsvLineHandler
import data.MealCsvParser
import data.MealCsvReader
import model.Meal
import org.example.data.CsvLineFormatter
import org.example.data.MealCsvRepository
import java.io.File

class GetSweetWithNoEggsUseCase(private val mealRepository: MealsRepository) {

    fun getDessertWithNoEggs(): List<Meal> {

        val meals = mealRepository.getAllMeals()
        val dessertsWithNoEggs =
            meals.filter { !it.ingredients.toString().contains("egg") && it.tags.toString().contains("dessert") }

        return dessertsWithNoEggs
    }
}

fun main() {
    val mealReader = MealCsvReader(File("food.csv"), CsvLineHandler())
    val mealParser = MealCsvParser(CsvLineFormatter())
    val mealCsvRepository: MealsRepository = MealCsvRepository(mealCsvParser = mealParser, mealCsvReader = mealReader)

    val meals = GetSweetWithNoEggsUseCase(mealCsvRepository).getDessertWithNoEggs()
    meals.forEach {
        println("\nMeal Name: ${it.name}\nMeal Tags: ${it.tags}\nMeal Ingredients: ${it.ingredients}\n")

    }
}