package com.yupengw.retriever.deserializer

class DoubleArrayDeserializer {
//++++++++++ Separator ++++++++++
    fun deserialize(s: String): DoubleArray = s.trim('[', ']').split(',').map { it.toDouble() }.toDoubleArray()
//++++++++++ Separator ++++++++++
}