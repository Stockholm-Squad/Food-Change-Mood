package org.example.logic.model

sealed class Results<out data> {
    data class Success<out data>(val model: data):Results<data>()
    data class Fail(val exception: Throwable): Results<Nothing>()
}