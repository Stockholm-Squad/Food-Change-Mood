package org.example

import appModule
import dataModule
import org.example.di.logicModule
import org.example.di.uiModule
import org.example.presentation.FoodConsoleUI
import org.example.utils.Constants
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import java.io.File


fun main() {

    val csvFile = File(Constants.MEAL_CSV_FILE)

    if (isFileValid(csvFile, Constants.MEAL_CSV_FILE)) return

    startKoin {
        modules(appModule, dataModule, logicModule, uiModule)
    }

    val foodConsoleUI: FoodConsoleUI = getKoin().get()
    foodConsoleUI.start()

}

private fun isFileValid(csvFile: File, fileName: String): Boolean {
    if (!csvFile.exists()) {
        println("File $fileName not found!")
        return true
    }
    return false
}