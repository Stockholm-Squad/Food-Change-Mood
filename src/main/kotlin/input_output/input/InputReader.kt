package org.example.input_output.input

interface InputReader<T> {
    fun read(): T?
}