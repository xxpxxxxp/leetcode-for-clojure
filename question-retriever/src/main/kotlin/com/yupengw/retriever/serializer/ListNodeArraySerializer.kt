package com.yupengw.retriever.serializer

import com.yupengw.retriever.ListNode

//++++++++++ Separator ++++++++++
fun serialize(arr: Array<ListNode?>): String =
    arr.joinToString(",", "[", "]") {
        val sb = mutableListOf<String>()
        var cur = it

        while (cur != null) {
            sb.add(cur.`val`.toString())
            cur = cur.next
        }

        sb.joinToString(",", "[", "]")
    }
