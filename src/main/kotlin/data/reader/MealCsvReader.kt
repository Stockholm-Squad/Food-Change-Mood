package org.example.data.reader

import org.example.data.utils.CsvLineHandler
import org.example.model.FoodChangeMoodExceptions
import org.example.utils.Constants
import java.io.File

class MealCsvReader(
    private val csvFile: File,
    private val csvLineHandler: CsvLineHandler
): MealReader {

    override fun readLinesFromFile(): Result<List<String>> {
        val lines = mutableListOf<String>()
        return try {
            csvFile.bufferedReader().use { csvFileReader ->
                csvFileReader.readLine()

                csvFileReader.forEachLine { line ->
                    csvLineHandler.handleLine(line)?.let { processedLine ->
                        lines.add(processedLine)
                    }
                }
            }
            Result.success(lines)
        } catch (e: Exception) {
            Result.failure(FoodChangeMoodExceptions.IOException.ReadFailedException(Constants.NO_FILE_FOUND))
        }

    }
}