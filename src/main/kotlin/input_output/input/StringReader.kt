package org.example.input_output.input

class StringReader : InputReader<String> {
    override fun read(): String? {
        return readlnOrNull()
    }
}