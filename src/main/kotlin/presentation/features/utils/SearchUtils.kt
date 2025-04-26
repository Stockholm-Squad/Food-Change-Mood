package org.example.presentation.features.utils

import org.example.input_output.input.InputReader
import org.example.utils.Constants

class SearchUtils {

    /**
     * Validates if the input matches Constants.Y after trimming and lowercasing.
     */
    fun isValidSearchAgainInput(input: String?): Boolean =
        input?.trim()?.lowercase() == Constants.Y

    /**
     * Reads input from the reader, trims it, and converts it to lowercase.
     */
    fun readTrimmedLowercaseInput(reader: InputReader): String? =
        reader.readStringOrNull()?.trim()?.lowercase()

    /**
     * Reads input from the reader, trims it, and ensures it is not blank.
     */
    fun readNonBlankTrimmedInput(reader: InputReader): String? =
        reader.readStringOrNull()
            ?.trim()
            ?.takeIf { it.isNotBlank() }

    /**
     * Parses a valid meal index from the input.
     * - Ensures the input is a number within the valid range [1, size].
     * - Converts the valid index to a 0-based index.
     */
    fun parseValidMealIndex(input: String, size: Int): Int? {
        val parsedNumber = input.toIntOrNull()
        return parsedNumber?.takeIf { it in 1..size }?.let { it - 1 }
    }

    /**
     * Reads a valid meal index from the reader.
     */
    fun getValidMealIndex(reader: InputReader, size: Int): Int? {
        val input = readTrimmedLowercaseInput(reader) ?: return null
        return parseValidMealIndex(input, size)
    }

    /**
     * Determines if the user wants to search again based on the input.
     * Returns true if the input matches Constants.Y, otherwise null.
     */
    fun shouldSearchAgain(reader: InputReader): Boolean? =
        readTrimmedLowercaseInput(reader)?.let { input ->
            if (isValidSearchAgainInput(input)) true else null
        }
}