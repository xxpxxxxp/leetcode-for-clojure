package com.yupengw.retriever.deserializer

import com.yupengw.retriever.NestedInteger
import java.util.*

class NestedIntegerDeserializer {
//++++++++++ Separator ++++++++++
    fun deserialize(s: String): NestedInteger {
        fun readPositiveInt(q: Queue<Char>): Int {
            var rst = 0
            while (q.peek().isDigit()) {
                rst = rst * 10 + (q.poll() - '0')
            }
            return rst
        }

        fun readInt(q: Queue<Char>): Int {
            return if (q.peek() == '-') {
                q.poll()
                -readPositiveInt(q)
            } else {
                readPositiveInt(q)
            }
        }

        fun helper(q: Queue<Char>): NestedInteger {
            val c = q.peek()
            if (c.isDigit() || c == '-') {
                val n = NestedInteger()
                n.setInteger(readInt(q))
                return n
            } else if (c == '[') {
                q.poll()    // pop
                val r = NestedInteger()
                while (q.isNotEmpty()) {
                    when (q.peek()) {
                        ',' -> q.poll()
                        ']' -> {
                            q.poll()
                            return r
                        }
                        else -> r.add(helper(q))
                    }
                }

                return r
            }

            throw Exception("cannot happen")
        }

        return helper(LinkedList(("[$s]").toList())).getList()!!.first()
    }
//++++++++++ Separator ++++++++++
}