package data

import model.Meal
import model.Nutrition
import org.example.data.utils.ColumnIndex
import org.example.data.utils.NutritionIndex
import java.lang.IllegalArgumentException


class MealsFileParser() {

    fun parseLine(row: String): Meal {
      val tokenizedList:List<String> = formatLineOfData(row)
      return Meal(
          name = tokenizedList[ColumnIndex.NAME],
          id = tokenizedList[ColumnIndex.ID].toIntOrNull() ?: throw IllegalArgumentException("Missing id"),
          preparationTime = tokenizedList[ColumnIndex.MINUTES].toIntOrNull() ?: 0,
          contributorId = tokenizedList[ColumnIndex.CONTRIBUTOR_ID].toIntOrNull()
              ?: throw IllegalArgumentException("Missing id"),
         addedDate   = tokenizedList[ColumnIndex.SUBMITTED],
          tags = parseListOfData(tokenizedList[ColumnIndex.TAGS]),
          nutrition = constructNutritionFromToken(tokenizedList[ColumnIndex.NUTRITION]),
          numberOfSteps = tokenizedList[ColumnIndex.N_STEPS].toIntOrNull() ?: 0,
          steps = parseListOfData(tokenizedList[ColumnIndex.STEPS]),
          description = tokenizedList[ColumnIndex.DESCRIPTION],
          ingredients = parseListOfData(tokenizedList[ColumnIndex.INGREDIENTS]),
          numberOfIngredients = tokenizedList[ColumnIndex.N_INGREDIENTS].toIntOrNull() ?: 0
      )
    }



    private fun constructNutritionFromToken(line: String): Nutrition {
        val nutrition = parseListOfData(line).map { it.toFloatOrNull() }
        return Nutrition(
            calories = nutrition[NutritionIndex.CALORIES_INDEX] ?: 0.0F,
            totalFat = nutrition[NutritionIndex.TOTAL_FAT_INDEX] ?: 0.0F,
            sugar = nutrition[NutritionIndex.SUGAR_INDEX] ?: 0.0F,
            sodium = nutrition[NutritionIndex.SODIUM] ?: 0.0F,
            protein = nutrition[NutritionIndex.PROTEIN_INDEX] ?: 0.0F,
            saturatedFat = nutrition[NutritionIndex.CARBOHYDRATES] ?: 0.0F,
            carbohydrates = nutrition[NutritionIndex.CARBOHYDRATES] ?: 0.0F,
        )
    }
    private fun parseListOfData(row: String): List<String> {
        return row
            .split(',')
            .map {
                it.replace("'","")
                    .replace("\"","")
                    .replace("[","")
                    .replace("]","").trim()}
            .filter { it.isNotBlank() }
    }
}

    private fun formatLineOfData(lineOfString: String): List<String> {
        val strBuilder = StringBuilder()
        val result = mutableListOf<String>()
        val isQuotes = false
        for (char in lineOfString) {
            when (char) {
                '"' -> {
                    isQuotes != isQuotes
                    strBuilder.append(char)
                }

                ',' ->
                    if (isQuotes) strBuilder.append(char)
                    else {
                        result.add(strBuilder.toString())
                        strBuilder.clear()
                    }
                else -> strBuilder.append(char)

            }
        }
        if (strBuilder.isNotEmpty()) result.add(strBuilder.toString())
            return result

    }
