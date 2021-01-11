package com.yupengw.retriever.serializer

//++++++++++ Separator ++++++++++
fun serialize(arr: DoubleArray): String = arr.joinToString(",", "[", "]") { String.format("%.6f", it) }
