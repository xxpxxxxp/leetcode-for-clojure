package com.yupengw.retriever.serializer

import com.yupengw.retriever.NestedInteger

//++++++++++ Separator ++++++++++
fun serialize(ls: List<NestedInteger>): String = ls.joinToString(",", "[", "]") {
    if (it.isInteger()) it.getInteger()!!.toString()
    else serialize(it.getList()!!)
}
