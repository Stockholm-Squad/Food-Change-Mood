package data

import org.example.model.Result
import org.example.data.utils.CsvLineHandler
import java.io.File

class MealCsvReader(
    private val csvFile: File,
    private val csvLineHandler: CsvLineHandler
) {
    fun readLinesFromFile(): Result<List<String>> {
        return try {

            val lines = mutableListOf<String>()
            csvFile.bufferedReader().use { csvFileReader ->
                csvFileReader.readLine()

                csvFileReader.forEachLine { line ->
                    csvLineHandler.handleLine(line)?.let { processedLine ->
                        lines.add(processedLine)
                    }
                }
            }
            Result.Success(lines)
        } catch (e: Exception) {
            Result.Failure("Error while reading from the CSV file", e)
        }

    }
}

