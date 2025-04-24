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

        return if (normalizedPattern.isNotEmpty()) {
            findPatternInText(normalizedMeal, normalizedPattern, createLpsTable(normalizedPattern))
        } else {
            false
        }
    }

    private fun normalizeString(input: String?): String {
        return input?.trim()?.lowercase() ?: ""
    }

    private fun findPatternInText(mealName: String, pattern: String, lps: IntArray): Boolean {
        var i = 0
        var j = 0

        while (i < mealName.length) {
            if (mealName[i] == pattern[j]) {
                i++
                j++
                if (j == pattern.length) {
                    return true
                }
            } else {
                if (j != 0) {
                    j = lps[j - 1]
                } else {
                    i++
                }
            }
        }

        return false
    }

    private fun createLpsTable(pattern: String): IntArray {
        val lps = IntArray(pattern.length)
        var length = 0
        var i = 1

        while (i < pattern.length) {
            if (pattern[i] == pattern[length]) {
                length++
                lps[i] = length
                i++
            } else {
                if (length != 0) {
                    length = lps[length - 1]
                } else {
                    lps[i] = 0
                    i++
                }
            }
        }

        return lps
    }
}
