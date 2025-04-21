package data

import org.example.data.utils.CsvLineHandler
import org.example.logic.model.Results
import java.io.File

class MealCsvReader(
    private val csvFile: File,
    private val csvLineHandler: CsvLineHandler
) {
    fun readLinesFromFile(): Results<List<String>> {
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
            Results.Success(lines)
        } catch (e: Exception) {
            Results.Fail(e)
        }

    }
}

