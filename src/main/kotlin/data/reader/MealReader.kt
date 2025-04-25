package org.example.data.reader

interface MealReader {
    fun readLinesFromFile(): Result<List<String>>
}