package com.yupengw.retriever.deserializer

class BooleanListDeserializer {
//++++++++++ Separator ++++++++++
    fun deserialize(s: String): BooleanArray = s.trim('[', ']').split(',').map { it.toBoolean() }.toBooleanArray()
//++++++++++ Separator ++++++++++
}