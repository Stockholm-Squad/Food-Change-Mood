package data

import org.example.data.utils.CsvLineHandler
import org.example.model.FoodChangeMoodExceptions
import java.io.BufferedReader
import java.io.File

class MealCsvReader(
    private val csvFile: File,
    private val csvLineHandler: CsvLineHandler
) {
    fun readLinesFromFile(): Result<List<String>> {
        val lines = mutableListOf<String>()
        return try {
            csvFile.bufferedReader().use { csvFileReader ->
                readLine(csvFileReader)
                csvFileReader.forEachLine { line ->
                    csvLineHandler.handleLine(line)?.let { processedLine ->
                        lines.add(processedLine)
                    }
                }
            }
            Result.success(lines)
        } catch (throwable: Throwable) {
            Result.failure(throwable)
        }

    }

    private fun readLine(csvFileReader: BufferedReader) {
        try {
            csvFileReader.readLine()
        } catch (throwable: Throwable) {
            throw FoodChangeMoodExceptions.IOException.ReadFailedException()
        }
    }
}

