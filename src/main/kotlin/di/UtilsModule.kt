package org.example.di

import org.example.utils.DateValidator
import org.koin.dsl.module

val utilsModule = module {
    single{ DateValidator() }
}