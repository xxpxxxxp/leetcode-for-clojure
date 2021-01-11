package com.yupengw.retriever.deserializer

import com.yupengw.retriever.TreeNode

class TreeNodeDeserializer {
//++++++++++ Separator ++++++++++
    fun deserialize(s: String): TreeNode? {
        val queue = s.trim('[', ']').split(',').toMutableList()
        if (queue.isEmpty())
            return null

        val head = TreeNode(queue.first().toInt())
        queue.removeAt(0)

        val p = mutableListOf(head)
        while (queue.isNotEmpty()) {
            val cur = p.first()
            p.removeAt(0)

            queue.first().let {
                if (it != "null") {
                    cur.left = TreeNode(it.toInt())
                    p.add(cur.left!!)
                }
            }
            queue.removeAt(0)

            queue.first().let {
                if (it != "null") {
                    cur.right = TreeNode(it.toInt())
                    p.add(cur.right!!)
                }
            }
            queue.removeAt(0)
        }

        return head
    }
//++++++++++ Separator ++++++++++
}