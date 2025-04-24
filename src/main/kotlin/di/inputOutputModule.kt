package org.example.di

import org.example.utils.InputReader
import org.example.utils.InputReaderImplementation
import org.example.utils.OutputPrinter
import org.example.utils.OutputPrinterImplementation
import org.koin.dsl.module

val inputOutputModule = module {
    factory<OutputPrinter> { OutputPrinterImplementation() }
    factory<InputReader> { InputReaderImplementation() }
}