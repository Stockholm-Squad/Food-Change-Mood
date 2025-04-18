package org.example.logic

import CsvLineHandler
import data.MealCsvParser
import data.FoodCsvReader
import model.Meal
import org.example.data.FoodCsvRepository
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
    val mealReader = FoodCsvReader(File("food.csv"), CsvLineHandler())
    val mealParser = MealCsvParser()
    val foodCsvRepository: MealsRepository = FoodCsvRepository(mealCsvParser = mealParser, foodCsvReader = mealReader)

    val meals = GetSweetWithNoEggsUseCase(foodCsvRepository).getDessertWithNoEggs()
    meals.forEach {
        println("\nMeal Name: ${it.name}\nMeal Tags: ${it.tags}\nMeal Ingredients: ${it.ingredients}\n")

    }
}