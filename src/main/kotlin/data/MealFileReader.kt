package org.example.data

import java.io.File
import java.io.IOException

class MealFileReader() {
    fun readLinesFromFile(): List<String> {
          val lines= mutableListOf<String>()
          val csvFileReader = getCsvFile().bufferedReader()

          var currentLine = StringBuilder()
          var insideQuotes = false
        csvFileReader.readLine()

          csvFileReader.forEachLine { line->
          currentLine.append(line)

          val quoteCount = currentLine.count { it == '"' }
          insideQuotes = quoteCount % 2 != 0

              if (!insideQuotes) {
                  lines.add(currentLine.toString())
                  currentLine = StringBuilder()
              } else {
                  currentLine.append("\n")
              }
          }

        return lines


      }




    }

    private fun getCsvFile(): File {
        val foodCsvFile = File("food.csv")
        if (foodCsvFile.exists()) {
            return foodCsvFile
        }
        throw IOException("File Not Found")
    }
