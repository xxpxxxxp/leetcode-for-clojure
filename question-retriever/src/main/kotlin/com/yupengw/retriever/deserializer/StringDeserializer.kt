package com.yupengw.retriever.deserializer

class StringDeserializer {
//++++++++++ Separator ++++++++++
    fun deserialize(s: String): String = s.trim('"')
//++++++++++ Separator ++++++++++
}