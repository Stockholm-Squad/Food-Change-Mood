package org.example.data.utils

class CsvLineHandler {

    private var currentLine = StringBuilder()
    private var insideQuotes = false

    fun handleLine(
        line: String
    ): String? {
        currentLine.append(line)
        handleIfInsideQuote()

        return (!insideQuotes)
            .takeIf { it }
            ?.let { getNewLineAndClearCurrentLine() }
            ?: run { currentLine.append("\n"); null }
    }

    private fun getNewLineAndClearCurrentLine(): String {
        val resultLine = currentLine.toString()
        currentLine = StringBuilder()
        return resultLine
    }

    private fun handleIfInsideQuote() {
        val quoteCount = currentLine.count { it == '"' }
        insideQuotes = quoteCount % 2 != 0
    }
}