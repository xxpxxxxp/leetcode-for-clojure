package com.yupengw.retriever.serializer

import com.yupengw.retriever.TreeNode

//++++++++++ Separator ++++++++++
fun serialize(t: TreeNode?): String {
    val sb = mutableListOf<String>()
    val queue = mutableListOf(t)

    while (queue.isNotEmpty()) {
        val c = queue.first()
        queue.removeAt(0)

        if (c == null)
            sb.add("null")
        else {
            queue.add(c.left)
            queue.add(c.right)
        }
    }

    return sb.dropLastWhile { it == "null" }.joinToString(",", "[", "]")
}
