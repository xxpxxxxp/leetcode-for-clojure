package com.yupengw.retriever.serializer

//++++++++++ Separator ++++++++++
fun serialize(list: List<Int>): String = list.joinToString(",", "[", "]")
