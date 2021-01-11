package com.yupengw.retriever.serializer

//++++++++++ Separator ++++++++++
fun serialize(mat: List<List<Int>>): String = mat.joinToString(",", "[", "]") {
    it.joinToString(",", "[", "]")
}
