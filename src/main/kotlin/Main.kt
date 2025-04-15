package org.example
import data.CsvFileFoodParser
import data.CsvFoodReader
import logic.GetHealthFastFoodUseCase
import org.example.data.CsvMealsRepository
import org.example.logic.IMealsRepository
import java.io.File


fun main() {

    val fileName = "food.csv"
    val csvFile = File(fileName)
    val csvFoodReader = CsvFoodReader(csvFile)
    val csvFileFoodParser = CsvFileFoodParser()
    val mealRepository: IMealsRepository = CsvMealsRepository(csvFileFoodParser, csvFoodReader)
    val getHealthFastFoodUseCase = GetHealthFastFoodUseCase(mealRepository)
    val result = getHealthFastFoodUseCase.getHealthyFastFood()
    println(result)
//     mealRepository.getAllMeals().also {
//        println(it)
//        println()
//    }

}