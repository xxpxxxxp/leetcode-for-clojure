package com.yupengw.retriever.deserializer

class StringArrayDeserializer {
//++++++++++ Separator ++++++++++
    fun deserialize(s: String): Array<String> = s.trim('[', ']').split(',').map { it.trim('[', ']') }.toTypedArray()
//++++++++++ Separator ++++++++++
}