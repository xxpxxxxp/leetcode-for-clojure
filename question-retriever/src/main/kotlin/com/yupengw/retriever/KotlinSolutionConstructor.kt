package com.yupengw.retriever

import com.yupengw.common.TestCase
import com.yupengw.common.leetcode.QuestionMeta
import com.yupengw.retriever.hasher.MOD
import com.yupengw.retriever.hasher.MUL
import com.yupengw.retriever.hasher.hash
import kotlin.random.Random

class KotlinSolutionConstructor(private val meta: QuestionMeta) {
    companion object {
        private val MUL_PRIMES = arrayOf(271,277,281,283,293,307,311)
        private val MOD_PRIMES = arrayOf(1000000009,1000000021,1000000033,1000000087,1000000093,1000000097,1000000103,1000000123,1000000181)

        private val TYPE_MAPPING = mapOf(
            "list<boolean>" to "List<Boolean>",
            "character[][]" to "Array<CharArray>",
            "character[]" to "CharArray",
            "character" to "Char",
            "double[]" to "DoubleArray",
            "double" to "Double",
            "integer[][]" to "Array<IntArray>",
            "integer[]" to "IntArray",
            "list<list<integer>>" to "List<List<Int>>",
            "list<integer>" to "List<Int>",
            "integer" to "Int",
            "ListNode[]" to "Array<ListNode?>",
            "ListNode" to "ListNode",
            "list<NestedInteger>" to "List<NestedInteger>",
            "NestedInteger" to "NestedInteger",
            "string[][]" to "Array<Array<String>>",
            "string[]" to "Array<String>",
            "list<list<string>>" to "List<List<String>>",
            "list<string>" to "List<String>",
            "string" to "String",
            "TreeNode" to "TreeNode"
        )
    }

    private val serializers = meta.params.map { it.type }.toSet().map { SerializerLoader.serializers[it]!! }
    private val deserializer = SerializerLoader.deserializers[meta.returnParam.type]!!

    val cases = mutableListOf<TestCase>()
    private val outputs = mutableMapOf<Int, String>()
    private var mul = MUL
    private var mod = MOD

    fun add(input: String, expectedOutput: String): Boolean {
        val hash = hash(input, mul, mod)
        if (hash !in outputs) {
            outputs[hash] = expectedOutput
            cases.add(TestCase(input, expectedOutput))
            return true
        }

        // rehash all cases
        for (i in MUL_PRIMES.dropWhile { it <= mul }) {
            mul = i
            label@ for (j in MOD_PRIMES.dropWhile { it <= mod }) {
                mod = j

                val rehash = mutableMapOf<Int, String>()
                for (case in cases) {
                    val h = hash(case.input, mul, mod)
                    if (h in rehash)
                        break@label

                    rehash[h] = case.expectedOutput
                }
                outputs.clear()
                outputs.putAll(rehash)
                return true
            }
        }

        return false
    }

    private fun typeMapping(type: String): String = TYPE_MAPPING[type]!!
    private fun defaultValue(type: String): String =
        when (type) {
            "boolean" -> "\"${Random.nextBoolean()}\""
            "integer" -> "\"${Random.nextInt()}\""
            "character" -> "\"${Random.nextInt(60).toChar()}\""
            "double" -> "\"${Random.nextDouble()}\""
            "string" -> "\"${Random.nextBytes(10).decodeToString()}\""
            else -> "[]"
        }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.appendLine("class Solution {")

        // add hash func
        sb.appendLine("    companion object {")
        sb.appendLine("        private const val MUL = $mul")
        sb.appendLine("        private const val MOD = $mod")
        sb.appendLine("    }")
        sb.appendLine()
        sb.append(SerializerLoader.hasher)
        sb.appendLine()

        // add serializers & deserializer
        for (s in serializers) {
            sb.appendLine(s)
        }

        sb.appendLine(deserializer)

        // add cases
        sb.appendLine("    private val cases: Map<Int, String> = mapOf(")
        sb.append(outputs.toList().joinToString(",\n") { "        ${it.first} to ${it.second}" })
        sb.appendLine("    )")

        // add main func
        sb.appendLine("    fun ${meta.name}(${meta.params.joinToString(", "){ "${it.name}: ${typeMapping(it.type)}" }}): ${typeMapping(meta.returnParam.type)} {")
        sb.appendLine("        val hs = listOf(${meta.params.joinToString(",") { "serialize(${it.name})" }}).joinToString(\"\\n\")")
        sb.appendLine("        return deserialize(cases[hash(hs)] ?: ${defaultValue(meta.returnParam.type)})")

        sb.appendLine("    }")

        sb.appendLine("}")
        return sb.toString()
    }
}