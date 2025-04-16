package org.example

import org.example.data.CsvMealsRepositoryImpl
import org.example.data.MealFileParser
import org.example.data.MealFileReader
import org.example.data.source.CsvMealDataSource
import org.example.logic.usecase.GetHealthyFastFoodUseCase
import presentation.FoodConsoleUi


fun main() {


    val mealFileReader=MealFileReader()
    val mealFileParser=MealFileParser()
    val csvMealDataSource=CsvMealDataSource(mealFileParser,mealFileReader)
    val mealsRepositoryImpl=CsvMealsRepositoryImpl(csvMealDataSource)
    val getHealthyFastFoodUseCase=GetHealthyFastFoodUseCase(mealsRepositoryImpl)
    val ui=FoodConsoleUi(getHealthyFastFoodUseCase)
    ui.start()



}