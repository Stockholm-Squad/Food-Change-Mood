package org.example.data.utils

class CsvLineFormatter {
    private var insideQuotes = false

    fun formatRowOfData(str: String): List<String> {
        val mealData = mutableListOf<String>()
        val mealColumnBuilder = StringBuilder()

        str.forEach { char ->
            handleCharacter(char, mealColumnBuilder, mealData)
        }
        mealColumnBuilder.takeIf {
            it.isNotEmpty()
        }.apply { mealData.add(this.toString()) }
        return mealData
    }

    private fun handleCharacter(
        char: Char,
        mealColumnBuilder: StringBuilder,
        mealData: MutableList<String>
    ) {
        when (char) {
            SpecialCharacter.DOUBLE_QUOTE.key -> insideQuotes = handleQuoteCase(insideQuotes, mealColumnBuilder, char)
            SpecialCharacter.COMMA.key -> handleCommaCase(insideQuotes, mealColumnBuilder, char, mealData)
            else -> mealColumnBuilder.append(char)
        }
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