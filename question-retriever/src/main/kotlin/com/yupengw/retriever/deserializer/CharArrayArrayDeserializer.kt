package com.yupengw.retriever.deserializer

class CharArrayArrayDeserializer {
//++++++++++ Separator ++++++++++
    fun deserialize(s: String): Array<CharArray> =
        s.substring(1, s.lastIndex)
            .split("],[")
            .map {
                it.trim('[', ']')
                    .split(',')
                    .map { i -> i[1] }
                    .toCharArray()
            }.toTypedArray()
//++++++++++ Separator ++++++++++
}