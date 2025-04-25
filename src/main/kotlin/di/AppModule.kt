package di

import dataModule
import org.example.di.logicModule
import org.example.di.uiModule
import org.example.di.utilsModule
import org.example.utils.Constants
import org.koin.dsl.module
import java.io.File

val appModule = module {
    single { File(Constants.MEAL_CSV_FILE) }
    includes(dataModule, logicModule, uiModule, utilsModule, inputOutputModule)
}