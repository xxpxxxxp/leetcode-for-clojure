package com.yupengw.retriever.serializer

//++++++++++ Separator ++++++++++
fun serialize(mat: Array<Array<String>>): String = mat.joinToString(",", "[", "]") {
    it.joinToString(",", "[", "]") { c -> "\"$c\"" }
}
