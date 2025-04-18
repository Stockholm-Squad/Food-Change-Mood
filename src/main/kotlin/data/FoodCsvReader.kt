package data

import CsvLineHandler
import java.io.File

class FoodCsvReader(
    private val csvFile: File,
    private val csvLineHandler: CsvLineHandler
) {
    fun readLinesFromFile(): List<String> {
        val lines = mutableListOf<String>()
        val csvFileReader = csvFile.bufferedReader()

        csvFileReader.readLine()
        csvFileReader.forEachLine { line ->
            csvLineHandler.handleLine(line)?.apply {
                lines.add(this)
            }
        }
        return lines
    }
}

