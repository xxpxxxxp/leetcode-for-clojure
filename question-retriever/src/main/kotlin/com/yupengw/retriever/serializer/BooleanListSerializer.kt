package com.yupengw.retriever.serializer

//++++++++++ Separator ++++++++++
fun serialize(list: List<Boolean>): String = list.joinToString(",", "[", "]") {
    if (it) "true" else "false"
}
