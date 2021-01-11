package com.yupengw.retriever.serializer

//++++++++++ Separator ++++++++++
fun serialize(list: List<String>): String = list.joinToString(",", "[", "]") { "\"$it\"" }
