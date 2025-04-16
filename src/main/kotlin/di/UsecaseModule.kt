package org.example.di

import org.example.logic.usecase.GetHealthFastFoodUseCase
import org.koin.dsl.module

val useCaseModule= module {
    single { GetHealthFastFoodUseCase(get()) }
}