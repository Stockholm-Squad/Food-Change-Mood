import data.FoodCsvParser
import data.FoodCsvReader
import org.example.data.FoodCsvRepository
import java.io.File
import org.example.logic.GetPotatoMealsUseCase
import org.example.presentation.FoodConsoleUI

fun main() {
    val fileName = "food.csv"
    val csvFile = File(fileName)

    // Check if the file exists before proceeding
    if (!csvFile.exists()) {
        println("File $fileName not found!")
        return
    }

    // Initialize dependencies
    val foodCsvReader = FoodCsvReader(csvFile)
    val foodCsvParser = FoodCsvParser()
    val mealsRepository = FoodCsvRepository(foodCsvReader, foodCsvParser)
    val getPotatoMealsUseCase = GetPotatoMealsUseCase(mealsRepository)

    // Start the UI
    val foodConsoleUI = FoodConsoleUI(getPotatoMealsUseCase)
    foodConsoleUI.start()
}