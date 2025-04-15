package org.example.logic

import model.Meal
import model.Nutrition

// 1. LPS Table Builder
fun computeLPSArray(pattern: String): IntArray {
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

// 2. KMP String Matching
fun kmpSearch(text: String, pattern: String): Boolean {
    if (pattern.isEmpty()) return true
    val lps = computeLPSArray(pattern)

    var i = 0 // index for text
    var j = 0 // index for pattern

    while (i < text.length) {
        if (text[i] == pattern[j]) {
            i++
            j++
        }

        if (j == pattern.length) {
            return true // pattern found
        } else if (i < text.length && text[i] != pattern[j]) {
            if (j != 0) {
                j = lps[j - 1]
            } else {
                i++
            }
        }
    }

    return false
} // pattern not found

fun searchMealsByNameKMP(meals: List<Meal>, keyword: String): List<Meal> {
    val lowerKeyword = keyword.lowercase()
    return meals.filter { meal ->
        meal.name?.let { it?.lowercase()?.let { it1 -> kmpSearch(it1, lowerKeyword) } } == true
    }
}
//fun main() {
//    val meals = listOf(
//        Meal(name = "Shjy Chicken tfuk",
//            id=5768778,
//            preparationTime = 5,
//            contributorId = 46787,
//            addedDate = "23024",
//            tags= listOf("sdfgh","redfyt"),
//            nutrition = Nutrition(2.3F,2.3F,2.3F,2.3F,6.6F,8.8F,3.4F),
//          numberOfIngredients  =9,
//            description = "rdtfgyhu",
//            ingredients = listOf("sdfgh","redfyt"),
//            numberOfSteps = 8,
//            steps = listOf("sdfgh","redfyt")
//        ),
//        Meal(name ="fish",
//            id=5768778,
//            preparationTime = 5,
//            contributorId = 46787,
//            addedDate = "23024",
//            tags= listOf("sdfgh","redfyt"),
//            nutrition = Nutrition(2.3F,2.3F,2.3F,2.3F,6.6F,8.8F,3.4F),
//            numberOfIngredients  =9,
//            description = "rdtfgyhu",
//            ingredients = listOf("sdfgh","redfyt"),
//            numberOfSteps = 8,
//            steps = listOf("sdfgh","redfyt")
//        ),
//        Meal(name = "Suj Chicken klap",
//            id=5768778,
//            preparationTime = 5,
//            contributorId = 46787,
//            addedDate = "23024",
//            tags= listOf("sdfgh","redfyt"),
//            nutrition = Nutrition(2.3F,2.3F,2.3F,2.3F,6.6F,8.8F,3.4F),
//            numberOfIngredients  =9,
//            description = "rdtfgyhu",
//            ingredients = listOf("sdfgh","redfyt"),
//            numberOfSteps = 8,
//            steps = listOf("sdfgh","redfyt")
//        ),
//    )
//
//    val result = searchMealsByNameKMP(meals, "chicken")
//
//    result.forEach { println(it) }
//
//}
