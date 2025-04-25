package org.example.di

import org.example.utils.DateParser
import org.example.utils.DateParserImpl
import org.example.utils.DateValidator
import org.koin.dsl.module

val utilsModule = module {
    single<DateParser> { DateParserImpl() }
    single { DateValidator(get()) }

}