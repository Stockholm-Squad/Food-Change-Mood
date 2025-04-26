package org.example.presentation.features.utils

import model.Meal
import org.example.input_output.input.InputReader
import org.example.utils.Constants

class SearchUtils {
    fun isValidSearchAgainInput(input: String?): Boolean =
        input != null && input.trim().lowercase() == Constants.Y

    fun readTrimmedLowercaseInput(reader: InputReader): String? =
        reader.readStringOrNull()?.trim()?.lowercase()

    fun readNonBlankTrimmedInput(reader: InputReader): String? =
        reader.readStringOrNull()?.trim()?.takeIf(String::isNotBlank)

    fun isNumericAndInRange(input: String, size: Int): Boolean =
        input.toIntOrNull()?.let { it in 1..size } ?: false

    fun isValidMealSelection(input: String?, mealsSize: Int): Boolean =
        input != null && input != Constants.N && isNumericAndInRange(input, mealsSize)

    fun getValidMealIndex(reader: InputReader, size: Int): Int? {
        return readTrimmedLowercaseInput(reader)
            ?.toIntOrNull()
            ?.takeIf { it in 1..size }
            ?.let { it - 1 }
    }


    fun shouldSearchAgain(reader: InputReader): Boolean? =
        readTrimmedLowercaseInput(reader)
            ?.let { input -> if (isValidSearchAgainInput(input)) true else null }


}
