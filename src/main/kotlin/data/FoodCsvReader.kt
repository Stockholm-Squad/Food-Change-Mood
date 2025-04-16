package data

import java.io.File

class FoodCsvReader(private val csvFile: File) {

    fun readLinesFromFile(): List<List<String>> {
        validateFileExists()

        val result = mutableListOf<List<String>>()
        val reader = csvFile.bufferedReader()
        val currentLine = StringBuilder()
        var quoteCount = 0

        reader.forEachLine { line ->
            if (line.isBlank()) return@forEachLine

            currentLine.append(line).append("\n")
            quoteCount += line.count { it == '"' }

            if (quoteCount % 2 == 0) {
                val parsedLine = parseCsvLine(currentLine.toString().trim())
                result.add(parsedLine)

                // Reset for the next line, update quoteCount directly
                currentLine.clear()
                quoteCount = 0
            }
        }

        return result
    }

    private fun validateFileExists() {
        if (!csvFile.exists()) {
            throw IllegalArgumentException("CSV file does not exist: ${csvFile.path}")
        }
    }

    private fun parseCsvLine(line: String): List<String> {
        val result = mutableListOf<String>()
        val current = StringBuilder()
        var inQuotes = false
        var quoteChar: Char? = null
        var i = 0

        while (i < line.length) {
            val char = line[i]

            when {
                // Handle opening or closing quotes
                char == '"' || char == '\'' -> {
                    // Handle quotes and toggle inQuotes
                    handleQuotes(line, current, char, i, quoteChar, inQuotes).also { (newInQuotes, newQuoteChar, newI) ->
                        inQuotes = newInQuotes
                        quoteChar = newQuoteChar
                        i = newI
                    }
                }

                // Handle comma separator
                char == ',' -> {
                    handleComma(current, inQuotes, result)
                    i++
                }

                // Regular character
                else -> {
                    current.append(char)
                    i++
                }
            }
        }

        // Add the last value
        if (current.isNotEmpty()) {
            result.add(current.toString().trim())
        }

        return result
    }

    private fun handleQuotes(
        line: String,
        current: StringBuilder,
        char: Char,
        i: Int,
        quoteChar: Char?,
        inQuotes: Boolean
    ): Triple<Boolean, Char?, Int> {
        return if (!inQuotes) {
            // Start of a quote
            Triple(true, char, i + 1)
        } else if (char == quoteChar) {
            // Check for escaped quote (double quote)
            if (i < line.lastIndex && line[i + 1] == quoteChar) {
                current.append(quoteChar)  // Add the escaped quote
                Triple(true, quoteChar, i + 2)  // Skip the next quote as it's escaped
            } else {
                // End of quote
                Triple(false, null, i + 1)
            }
        } else {
            // Normal character inside quotes
            current.append(char)
            Triple(inQuotes, quoteChar, i + 1)
        }
    }

    private fun handleComma(current: StringBuilder, inQuotes: Boolean, result: MutableList<String>) {
        if (inQuotes) {
            // Append comma if inside quotes
            current.append(',')
        } else {
            // If not inside quotes, add the current value and reset
            result.add(current.toString().trim())
            current.clear()
        }
    }
}
