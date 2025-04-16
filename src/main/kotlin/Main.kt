package org.example

import data.FoodCsvParser
import data.FoodCsvReader
import org.example.data.FoodCsvRepository
import org.example.logic.MealsRepository
import java.io.File


fun main() {
    val fileName = "food.csv"
    val csvFile = File(fileName)

    // Check if the file exists before proceeding
    if (!csvFile.exists()) {
        println("File $fileName not found!")
        return
    }

    val foodCsvReader = FoodCsvReader(csvFile)
    val foodCsvParser = FoodCsvParser()
    val mealsRepository: MealsRepository = FoodCsvRepository(foodCsvReader, foodCsvParser)

    try {
        val meals = mealsRepository.getAllMeals()

        // Display meal information
        meals.forEachIndexed { index, meal ->
            println("Meal $index:")
            println("Name: ${meal.name}")
            println("Minutes: ${meal.minutes}")
            println("CONTRIBUTOR_ID: ${meal.contributorId}")
            println("SUBMITTED: ${meal.submitted}")
            println("TAGS: ${meal.tags}")
            println("NUTRITION: ${meal.nutrition}")
            println("N_STEPS: ${meal.numberOfSteps}")
            println("STEPS: ${meal.steps}")
            println("DESCRIPTION: ${meal.description}")
            println("INGREDIENTS: ${meal.ingredients}")
            println("N_INGREDIENTS: ${meal.numberOfIngredients}")
            println("----------------------------------------------------")
        }
    } catch (e: Exception) {
        println("Error reading or parsing the CSV file: ${e.message}")
    }
}
