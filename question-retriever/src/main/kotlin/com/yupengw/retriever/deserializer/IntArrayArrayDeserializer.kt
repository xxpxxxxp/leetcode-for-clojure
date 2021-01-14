package com.yupengw.retriever.deserializer

class IntArrayArrayDeserializer {
//++++++++++ Separator ++++++++++
    fun deserialize(s: String): Array<IntArray> =
        s.substring(1, s.lastIndex)
            .split("],[")
            .map {
                it.trim('[', ']')
                    .split(',')
                    .map { i -> i.toInt() }
                    .toIntArray()
            }.toTypedArray()
//++++++++++ Separator ++++++++++
}