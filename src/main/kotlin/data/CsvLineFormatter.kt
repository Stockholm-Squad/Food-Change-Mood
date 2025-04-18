package org.example.data

class CsvLineFormatter {
    fun formatRowOfData(str: String): List<String> {
        val mealData = mutableListOf<String>()
        val mealColumnBuilder = StringBuilder()
        var insideQuotes = false

        for (char in str) {
            when (char) {
                '"' -> insideQuotes = handleQuoteCase(insideQuotes, mealColumnBuilder, char)
                ',' -> handleCommaCase(insideQuotes, mealColumnBuilder, char, mealData)
                else -> mealColumnBuilder.append(char)
            }
        }

        if (mealColumnBuilder.isNotEmpty()) mealData.add(mealColumnBuilder.toString())
        return mealData
    }

    private fun handleQuoteCase(
        insideQuotes: Boolean,
        mealColumnBuilder: StringBuilder,
        char: Char
    ): Boolean {
        mealColumnBuilder.append(char)
        return !insideQuotes
    }

    private fun handleCommaCase(
        insideQuotes: Boolean,
        mealColumnBuilder: StringBuilder,
        char: Char,
        mealData: MutableList<String>
    ) {
        if (insideQuotes) mealColumnBuilder.append(char)
        else {
            mealData.add(mealColumnBuilder.toString())
            mealColumnBuilder.clear()
        }
    }
}