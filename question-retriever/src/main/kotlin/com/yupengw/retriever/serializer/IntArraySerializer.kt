package com.yupengw.retriever.serializer

//++++++++++ Separator ++++++++++
fun serialize(arr: IntArray): String = arr.joinToString(",", "[", "]")
