package org.example.di

import data.MealsFileParser
import data.MealsFileReader
import org.example.data.CsvMealsRepositoryImpl
import org.example.data.source.CsvMealDataSource
import org.example.logic.repo.IMealsRepository
import org.koin.dsl.module
import presentation.FoodConsoleUi
import java.io.File


val appModule= module {
    single { File("food.csv") }
    single { MealsFileParser() }
    single { MealsFileReader() }
    single { CsvMealDataSource(get(),get()) }
    single<IMealsRepository> { CsvMealsRepositoryImpl(get()) }
    single { FoodConsoleUi(get()) }

}