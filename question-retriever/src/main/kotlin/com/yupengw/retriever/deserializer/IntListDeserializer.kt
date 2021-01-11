package com.yupengw.retriever.deserializer

class IntListDeserializer {
    //++++++++++ Separator ++++++++++
    fun deserialize(s: String): List<Int> = s.trim('[', ']').split(',').map { it.toInt() }
//++++++++++ Separator ++++++++++
}