package com.yupengw.retriever.serializer

//++++++++++ Separator ++++++++++
fun serialize(arr: Array<String>): String = arr.joinToString(",", "[", "]") { "\"$it\"" }
