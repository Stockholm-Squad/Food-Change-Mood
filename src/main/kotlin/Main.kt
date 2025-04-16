package org.example
import org.example.di.appModule
import org.example.di.useCaseModule
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import presentation.FoodConsoleUi


fun main() {


    startKoin {
        modules(appModule, useCaseModule)
    }
    val ui: FoodConsoleUi= getKoin().get()
    ui.start()


}