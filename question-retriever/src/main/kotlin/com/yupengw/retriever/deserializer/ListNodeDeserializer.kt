package com.yupengw.retriever.deserializer

import com.yupengw.retriever.ListNode

class ListNodeDeserializer {
//++++++++++ Separator ++++++++++
    fun deserialize(s: String): ListNode? {
        val queue = s.trim('[', ']').split(',').map { it.toInt() }.toMutableList()
        if (queue.isEmpty())
            return null

        val head = ListNode(queue.first())
        queue.removeAt(0)

        var cur = head
        while (queue.isNotEmpty()) {
            cur.next = ListNode(queue.first())
            queue.removeAt(0)
            cur = cur.next!!
        }

        return head
    }
//++++++++++ Separator ++++++++++
}