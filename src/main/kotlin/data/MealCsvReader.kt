package data

import org.example.data.utils.CsvLineHandler
import java.io.File

class MealCsvReader(
    private val csvFile: File,
    private val csvLineHandler: CsvLineHandler
) {
    fun readLinesFromFile(): List<String> {
        val lines = mutableListOf<String>()
        val csvFileReader = csvFile.bufferedReader()

        csvFileReader.readLine() //TODO handle it within try and catch
        csvFileReader.forEachLine { line ->
            csvLineHandler.handleLine(line)?.apply {
                lines.add(this)
            }
        }
        return lines
    }
}

