package com.yupengw.retriever.deserializer

class StringListDeserializer {
//++++++++++ Separator ++++++++++
    fun deserialize(s: String): List<String> = s.trim('[', ']').split(',').map { it.trim('[', ']') }
//++++++++++ Separator ++++++++++
}