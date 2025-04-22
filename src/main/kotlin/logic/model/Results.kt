package org.example.logic.model

sealed class Results<out data> {
    data class Success<out data>(val model: data):Results<data>()
    data class Fail(val exception: Throwable): Results<Nothing>()

    /**
     * Fold method to handle both Success and Fail cases.
     *
     * @param onSuccess Lambda to handle the Success case. It takes the data and returns a result of type [R].
     * @param onFail Lambda to handle the Fail case. It takes the exception and returns a result of type [R].
     * @return The result of applying either [onSuccess] or [onFail], depending on the type of Results.
     */
    fun <R> fold(onSuccess: (data) -> R, onFail: (Throwable) -> R): R {
        return when (this) {
            is Success -> onSuccess(model)
            is Fail -> onFail(exception)
        }
    }
}

