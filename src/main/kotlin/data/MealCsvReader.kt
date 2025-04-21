package data

import org.example.data.utils.CsvLineHandler
import java.io.File
import java.io.IOException

class MealCsvReader(
    private val csvFile: File,
    private val csvLineHandler: CsvLineHandler
) {
    fun readLinesFromFile(): List<String> {
        val lines = mutableListOf<String>()
        csvFile.bufferedReader().use { csvFileReader ->
            try {
                csvFileReader.readLine()

                csvFileReader.forEachLine { line ->
                    csvLineHandler.handleLine(line)?.let { processedLine ->
                        lines.add(processedLine)
                    }
                }
            } catch (e: Exception) {
                throw IOException("Error while reading from the CSV file", e)
            }
        }
        return lines
    }
}

