package org.example.logic.usecases

/**
 * A class implementing the Knuth-Morris-Pratt (KMP) algorithm for efficient substring search.
 * It preprocesses the pattern to create an LPS (Longest Prefix Suffix) table, reducing the number
 * of character comparisons during the search phase.
 */
class SearchingByKmpUseCase {


    fun searchByKmp(mealName: String?, pattern: String?): Boolean {
        val normalizedMeal = normalizeString(mealName)
        val normalizedPattern = normalizeString(pattern)

        return normalizedPattern.takeIf { it.isNotEmpty() }
            ?.let { findPatternInText(normalizedMeal, it, createLpsTable(it)) }
            ?: false
    }

    private fun normalizeString(input: String?): String =
        input?.trim()?.lowercase() ?: ""


    private fun findPatternInText(mealName: String, pattern: String, lps: IntArray): Boolean =
        findPatternRecursive(mealName, pattern, lps, textIndex = 0, patternIndex = 0)


    private tailrec fun findPatternRecursive(
        mealName: String,
        pattern: String,
        lps: IntArray,
        textIndex: Int,
        patternIndex: Int
    ): Boolean =
        when {
            textIndex >= mealName.length -> false
            mealName[textIndex] == pattern[patternIndex] -> {
                val newPatternIndex = patternIndex + 1
                if (newPatternIndex == pattern.length) true
                else findPatternRecursive(mealName, pattern, lps, textIndex + 1, newPatternIndex)
            }
            patternIndex != 0 -> findPatternRecursive(mealName, pattern, lps, textIndex, lps[patternIndex - 1])
            else -> findPatternRecursive(mealName, pattern, lps, textIndex + 1, 0)
        }


    private fun createLpsTable(pattern: String): IntArray =
        buildLpsTableRecursive(pattern, IntArray(pattern.length), prefixLength = 0, currentIndex = 1)


    private tailrec fun buildLpsTableRecursive(
        pattern: String,
        lps: IntArray,
        prefixLength: Int,
        currentIndex: Int
    ): IntArray =
        when {
            currentIndex >= pattern.length -> lps
            pattern[currentIndex] == pattern[prefixLength] -> {
                lps[currentIndex] = prefixLength + 1
                buildLpsTableRecursive(pattern, lps, prefixLength + 1, currentIndex + 1)
            }
            prefixLength != 0 -> buildLpsTableRecursive(pattern, lps, lps[prefixLength - 1], currentIndex)
            else -> {
                lps[currentIndex] = 0
                buildLpsTableRecursive(pattern, lps, 0, currentIndex + 1)
            }
        }
}