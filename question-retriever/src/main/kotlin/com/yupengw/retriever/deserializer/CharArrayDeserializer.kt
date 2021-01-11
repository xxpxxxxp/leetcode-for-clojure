package com.yupengw.retriever.deserializer

class CharArrayDeserializer {
//++++++++++ Separator ++++++++++
    fun deserialize(s: String): CharArray = s.trim('[', ']').split(',').map { it[1] }.toCharArray()
//++++++++++ Separator ++++++++++
}