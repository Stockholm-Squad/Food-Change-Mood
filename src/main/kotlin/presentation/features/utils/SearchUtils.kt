package org.example.presentation.features.utils

import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.utils.Constants

class SearchUtils(
    private val printer: OutputPrinter,
) {

    /**
     * Validates if the input matches "y" (for search again).
     */
    fun isValidSearchAgainInput(input: String?): Boolean =
        input?.trim()?.lowercase() == Constants.Y

    /**
     * Reads and returns non-blank trimmed input from the user.
     */
    fun readNonBlankTrimmedInput(reader: InputReader): String? =
        reader.readStringOrNull()?.trim()?.takeIf { it.isNotBlank() }


    /**
     * Continuously prompts the user for a valid meal index until a valid one or 'n' is entered.
     * Returns null if user chooses to skip (by typing "n").
     */
    fun getValidMealIndex(reader: InputReader, size: Int): Int? {
        while (true) {
            val input = readTrimmedLowercaseInput(reader) ?: return null

            when {
                isSkipInput(input) -> return null // User skips
                isValidMealIndex(input, size) -> return parseMealIndex(input)
                else -> handleInvalidInput()
            }
        }
    }

    /**
     * Reads and returns non-blank trimmed lowercase input from the user.
     */
    private fun readTrimmedLowercaseInput(reader: InputReader): String? =
        reader.readStringOrNull()?.trim()?.lowercase()?.takeIf { it.isNotBlank() }

    /**
     * Checks if the input indicates the user wants to skip (e.g., "n").
     */
    private fun isSkipInput(input: String): Boolean =
        input == Constants.N

    /**
     * Checks if the input represents a valid meal index within the range [1, size].
     */
    private fun isValidMealIndex(input: String, size: Int): Boolean =
        input.toIntOrNull()?.let { it in 1..size } ?: false

    /**
     * Parses the input into a zero-based meal index.
     */
    private fun parseMealIndex(input: String): Int =
        input.toInt() - 1

    /**
     * Handles invalid input by printing an error message.
     */
    private fun handleInvalidInput() {
        printer.printLine(Constants.INVALID_SELECTION_MESSAGE)
    }

    /**
     * Asks the user whether to search again. Returns true only if user enters "y".
     */
    fun shouldSearchAgain(reader: InputReader): Boolean? {
        printer.printLine(Constants.SEARCH_AGAIN_PROMPT)
        val input = readTrimmedLowercaseInput(reader)
        return if (isValidSearchAgainInput(input)) true else null
    }

}

