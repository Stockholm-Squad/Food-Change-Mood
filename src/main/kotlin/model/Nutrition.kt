package model

//TODO set its value to be nullable
data class Nutrition(
    val calories: Float = 0f,
    val totalFat: Float = 0f,
    val sugar: Float = 0f,
    val sodium: Float = 0f,
    val protein: Float = 0f,
    val saturatedFat: Float = 0f,
    val carbohydrates: Float = 0f
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
