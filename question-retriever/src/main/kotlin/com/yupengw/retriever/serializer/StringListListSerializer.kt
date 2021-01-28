package com.yupengw.retriever.serializer

//++++++++++ Separator ++++++++++
fun serialize(mat: List<List<String>>): String = mat.joinToString(",", "[", "]") {
    it.joinToString(",", "[", "]") { c -> "\"$c\"" }
}
