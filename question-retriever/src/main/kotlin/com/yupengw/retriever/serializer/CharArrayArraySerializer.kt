package com.yupengw.retriever.serializer

//++++++++++ Separator ++++++++++
fun serialize(mat: Array<CharArray>): String = mat.joinToString(",", "[", "]") {
    it.joinToString(",", "[", "]") { c -> "\"$c\"" }
}
