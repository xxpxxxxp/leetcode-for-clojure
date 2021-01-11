package com.yupengw.retriever.serializer

//++++++++++ Separator ++++++++++
fun serialize(mat: Array<IntArray>): String = mat.joinToString(",", "[", "]") {
    it.joinToString(",", "[", "]")
}
