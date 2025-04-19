package model


/**
 * Represents the nutritional information of a meal.
 *
 * All properties are nullable to allow flexibility in cases where specific
 * nutritional data is unavailable.
 */

data class Nutrition(
    val calories: Float?,
    val totalFat: Float?,
    val sugar: Float?,
    val sodium: Float?,
    val protein: Float?,
    val saturatedFat: Float?,
    val carbohydrates: Float?
) {
    override fun toString(): String {
        return """Nutrition(
                        calories=$calories,
                        totalFat=$totalFat,
                        sugar=$sugar,
                        sodium=$sodium,
                        protein=$protein,
                        saturatedFat=$saturatedFat,
                        carbohydrates=$carbohydrates
                          )
        """.trimIndent()
    }
}
