package org.example.input_output.input

class FloatReader: InputReader<Float> {
    override fun read(): Float? {
        return readlnOrNull()?.toFloatOrNull()
    }
}