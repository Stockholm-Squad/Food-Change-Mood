package data

import org.example.data.utils.CsvLineHandler
import java.io.File
import java.io.IOException

class MealCsvReader(
    private val csvFile: File,
    private val csvLineHandler: CsvLineHandler
) {
    fun readLinesFromFile(): ReaderResult<List<String>> {
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
            ReaderResult.Success(lines)
        } catch (e: Exception) {
            ReaderResult.Failure("Error while reading from the CSV file", e)
        }

    }
}


sealed class ReaderResult<out T> {
    data class Success<T>(val value: T) : ReaderResult<T>()
    data class Failure(val errorMessage: String, val cause: Throwable? = null) : ReaderResult<Nothing>()
}