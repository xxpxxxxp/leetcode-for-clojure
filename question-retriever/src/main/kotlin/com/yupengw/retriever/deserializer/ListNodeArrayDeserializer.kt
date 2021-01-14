package com.yupengw.retriever.deserializer

import com.yupengw.retriever.ListNode

class ListNodeArrayDeserializer {
//++++++++++ Separator ++++++++++
    fun deserialize(s: String): Array<ListNode?> =
        s.substring(1, s.lastIndex)
            .split("],[")
            .map {
                val queue = it.trim('[', ']').split(',').map { i -> i.toInt() }.toMutableList()
                if (queue.isEmpty())
                    null
                else {
                    val head = ListNode(queue.first())
                    queue.removeAt(0)

                    var cur = head
                    while (queue.isNotEmpty()) {
                        cur.next = ListNode(queue.first())
                        queue.removeAt(0)
                        cur = cur.next!!
                    }

                    head
                }
            }.toTypedArray()
//++++++++++ Separator ++++++++++
}