package org.example.logic.model

sealed class FoodChangeModeResults<out data> {
    data class Success<out data>(val model: data):FoodChangeModeResults<data>()
    data class Fail(val exception: Throwable): FoodChangeModeResults<Nothing>()
}