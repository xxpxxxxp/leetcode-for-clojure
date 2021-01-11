package com.yupengw.retriever.serializer

import com.yupengw.retriever.ListNode

//++++++++++ Separator ++++++++++
fun serialize(l: ListNode?): String {
    val sb = mutableListOf<String>()
    var cur = l

    while (cur != null) {
        sb.add(cur.`val`.toString())
        cur = cur.next
    }

    return sb.joinToString(",", "[", "]")
}
