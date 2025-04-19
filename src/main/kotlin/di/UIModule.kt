package org.example.di

import org.example.presentation.FoodConsoleUI
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val uiModule = module {
    singleOf(::FoodConsoleUI)
}