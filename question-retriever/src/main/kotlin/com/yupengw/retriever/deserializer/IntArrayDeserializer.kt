package com.yupengw.retriever.deserializer

class IntArrayDeserializer {
//++++++++++ Separator ++++++++++
    fun deserialize(s: String): IntArray = s.trim('[', ']').split(',').map { it.toInt() }.toIntArray()
//++++++++++ Separator ++++++++++
}