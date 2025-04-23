package org.example.logic.usecases

/**
 * A class implementing the Knuth-Morris-Pratt (KMP) algorithm for efficient substring search.
 * It preprocesses the pattern to create an LPS (Longest Prefix Suffix) table, reducing the number
 * of character comparisons during the search phase.
 */
class SearchingByKmpUseCase {

    fun searchByKmp(mealName: String?, pattern: String?): Boolean =
        mealName?.lowercase()?.let { meal ->
            pattern?.trim()?.lowercase()?.let { pat -> // Trim the pattern to remove leading/trailing spaces
                if (pat.isNotEmpty()) findPatternInText(meal, pat, createLpsTable(pat))
                else false
            }
        } ?: false

    private fun findPatternInText(mealName: String, pattern: String, lps: IntArray): Boolean {
        var matchIndex = 0
        return mealName.foldIndexed(false) { index, found, _ ->
            when {
                found -> true
                pattern[matchIndex] == mealName[index] -> {
                    matchIndex++
                    matchIndex == pattern.length
                }
                matchIndex != 0 -> {
                    matchIndex = lps[matchIndex - 1]
                    false
                }
                else -> false
            }
        }
    }

    private fun createLpsTable(pattern: String): IntArray =
        pattern.foldIndexed(IntArray(pattern.length)) { index, lps, _ ->
            if (index == 0) return@foldIndexed lps
            var j = lps[index - 1]
            while (j > 0 && pattern[j] != pattern[index]) j = lps[j - 1]
            if (pattern[j] == pattern[index]) j++
            lps[index] = j
            lps
        }
}
