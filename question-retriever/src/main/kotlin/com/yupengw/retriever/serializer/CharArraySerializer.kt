package com.yupengw.retriever.serializer

//++++++++++ Separator ++++++++++
fun serialize(arr: CharArray): String = arr.joinToString(",", "[", "]") { "\"$it\"" }
