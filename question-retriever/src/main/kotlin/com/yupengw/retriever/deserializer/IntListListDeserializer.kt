package com.yupengw.retriever.deserializer

class IntListListDeserializer {
//++++++++++ Separator ++++++++++
    fun deserialize(s: String): List<List<Int>> =
        s.substring(1, s.lastIndex)
            .split("],[")
            .map {
                it.trim('[', ']')
                    .split(',')
                    .map { i -> i.toInt() }
            }
//++++++++++ Separator ++++++++++
}