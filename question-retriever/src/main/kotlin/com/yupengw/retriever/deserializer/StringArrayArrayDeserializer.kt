package com.yupengw.retriever.deserializer

class StringArrayArrayDeserializer {
//++++++++++ Separator ++++++++++
    fun deserialize(s: String): Array<Array<String>> =
        s.substring(1, s.lastIndex)
            .split("],[")
            .map {
                it.trim('[', ']')
                    .split(',')
                    .map { i -> i.trim('"') }
                    .toTypedArray()
            }.toTypedArray()
//++++++++++ Separator ++++++++++
}