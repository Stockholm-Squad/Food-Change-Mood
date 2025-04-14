package org.example

import java.io.File

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val file = File("food.csv")
    val fileRead:List<List<String>> = File("food.csv").readLines().map { it.split(",") }
//    val foods = file.readLines().toString().split(",")
//    println(foods[10000])
//    val x = foods[ColumnIndex.DESCRIPTION][9]
//    val b  = foods[x]
   for (i in 10 until 17)
       println(fileRead[i])
//    println(b)
//    13588149
//    294520189


}