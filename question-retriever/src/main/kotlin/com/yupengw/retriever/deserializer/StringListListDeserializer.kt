package com.yupengw.retriever.deserializer

class StringListListDeserializer {
//++++++++++ Separator ++++++++++
    fun deserialize(s: String): List<List<String>> =
        s.substring(1, s.lastIndex)
            .split("],[")
            .map {
                it.trim('[', ']')
                    .split(',')
                    .map { i -> i.trim('"') }
            }
//++++++++++ Separator ++++++++++
}