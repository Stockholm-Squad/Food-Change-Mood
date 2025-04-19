package logic

import model.Meal
import org.example.logic.MealsRepository

//TODO rename GetMealsByNameUseCase
//TODO Move the algorithm to another class to be used later
class SearchMealByNameUseCase(private val mealsRepository: MealsRepository) {

    /**
     * Searches for meals whose name contains the given query (case-insensitive).
     *
     * @param query The name or part of the name to search for.
     * @return A list of meals matching the query.
     */
    fun searchMealsByName(query: String?): List<Meal> {
        if (query.isNullOrEmpty()) return emptyList()

        return mealsRepository.getAllMeals().filter { meal ->
            kmpSearch(meal.name, query)
        }
    }
}

/**
 * Searches for a pattern in a meal name using the KMP algorithm.
 *
 * @param mealName The meal name to search within (nullable).
 * @param pattern The pattern string to search for (nullable).
 * @return `true` if the pattern is found in the meal name, otherwise `false`.
 */
fun kmpSearch(mealName: String?, pattern: String?): Boolean {
    // Validate input: Return false if either mealName or pattern is null/empty
    if (mealName == null || pattern == null || pattern.isEmpty()) return false

    // Step 1: Build the LPS table for the pattern
    val lps = buildLpsTable(pattern)
    val lowerCaseMealName = mealName.lowercase()
    val lowerCasePattern = pattern.lowercase()
    // Step 2: Perform the KMP search using the LPS table

    return performKmpSearch(lowerCaseMealName, lowerCasePattern, lps)
}


/**
 * Performs the KMP search using the LPS table.
 *
 * @param mealName The main text (meal name) to search within.
 * @param pattern The pattern string to search for.
 * @param lps The precomputed LPS table for the pattern.
 * @return `true` if the pattern is found in the meal name, otherwise `false`.
 */
private fun performKmpSearch(mealName: String, pattern: String, lps: IntArray): Boolean {
    val textLength = mealName.length
    val patternLength = pattern.length

    var currentNameIndex = 0 // Tracks the position in the meal name
    var currentMatchIndex = 0 // Tracks the position in the pattern

    // Iterate through the meal name to find the pattern
    while (currentNameIndex < textLength) {
        if (pattern[currentMatchIndex] == mealName[currentNameIndex]) {
            // If characters match, move both indices forward
            currentNameIndex++
            currentMatchIndex++
        }

        if (currentMatchIndex == patternLength) {
            // If we've matched the entire pattern, return true
            return true
        } else if (currentNameIndex < textLength && pattern[currentMatchIndex] != mealName[currentNameIndex]) {
            // If there's a mismatch, decide whether to backtrack or move forward
            if (currentMatchIndex != 0) {
                // Use the LPS table to determine the next position to check
                currentMatchIndex = lps[currentMatchIndex - 1]
            } else {
                // If no backtracking is possible, move to the next character in the meal name
                currentNameIndex++
            }
        }
    }

    return false // No match found
}


/**
 * Builds the Longest Prefix Suffix (LPS) table for the KMP algorithm.
 *
 * @param pattern The pattern string for which the LPS table is built.
 * @return An integer array representing the LPS table.
 */
private fun buildLpsTable(pattern: String): IntArray {
    val patternLength = pattern.length
    val lps = IntArray(patternLength) // Initialize the LPS table with zeros
    var matchedPrefixLength = 0 // Tracks the length of the current longest prefix that is also a suffix
    var currentPatternIndex = 1 // Start from the second character of the pattern

    // Iterate through the pattern to compute the LPS values
    while (currentPatternIndex < patternLength) {
        if (pattern[currentPatternIndex] == pattern[matchedPrefixLength]) {
            // If characters match, increment the matched prefix length and update the LPS table
            matchedPrefixLength++
            lps[currentPatternIndex] = matchedPrefixLength
            currentPatternIndex++
        } else {
            if (matchedPrefixLength != 0) {
                // If there's a mismatch but we have a previously matched prefix, backtrack
                matchedPrefixLength = lps[matchedPrefixLength - 1]
            } else {
                // If no match and no backtracking possible, set LPS value to 0 and move to the next character
                lps[currentPatternIndex] = 0
                currentPatternIndex++
            }
        }
    }

    return lps
}