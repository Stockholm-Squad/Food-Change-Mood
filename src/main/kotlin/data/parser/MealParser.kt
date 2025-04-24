package org.example.data.parser

import model.Meal

interface MealParser {
    fun parseLine(row: String): Result<Meal>
}