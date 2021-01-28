package com.yupengw.retriever

import java.io.File

object SerializerLoader {
    private val PRIMARY_MAPPING = mapOf(
        "Boolean" to "boolean",
        "Int" to "integer",
        "Char" to "character",
        "Double" to "double",
        "String" to "string"
    )

    private const val SEPARATOR = "//++++++++++"

    val serializers: Map<String, String> = searchSerializers("serializer")
    val deserializers: Map<String, String> = searchSerializers("deserializer")
    val hasher = extractMethod(File(Thread.currentThread().contextClassLoader.getResource("hasher/Hasher.kt")!!.path).readLines())

    private fun searchSerializers(resourceFolder: String): Map<String, String> =
        File(Thread.currentThread().contextClassLoader.getResource(resourceFolder)!!.path)
            .listFiles()!!
            .map { file ->
                extractTypeMeta(file.nameWithoutExtension) to extractMethod(file.readLines())
            }
            .toMap()

    private fun extractTypeMeta(raw: String): String {
        val term = mutableListOf<String>()
        val sb = StringBuilder()

        for (c in raw) {
            if (c.isUpperCase() && sb.isNotEmpty()) {
                term.add(sb.toString())
                sb.clear()
            }

            sb.append(c)
        }

        // intentionally drop last term: Serializer or Deserializer
        // term.add(sb.toString())
        val wrapper = mutableListOf<String>()

        while (true) {
            when (term.last()) {
                "Array", "List" ->
                    wrapper.add(term.removeLast())
                else -> {
                    var base = term.joinToString("").let { PRIMARY_MAPPING.getOrDefault(it, it) }
                    for (t in wrapper) {
                        when (t) {
                            "Array" -> base = "$base[]"
                            "List" -> base = "list<$base>"
                        }
                    }

                    return base
                }
            }
        }
    }

    private fun extractMethod(lines: List<String>): String =
        lines.dropWhile { !it.startsWith(SEPARATOR) }
            .drop(1)
            .let {
                if (it.any { line -> line.startsWith(SEPARATOR) })
                    it.dropLastWhile { line -> !line.startsWith(SEPARATOR) }.dropLast(1)
                else
                    it
            }.joinToString("\n")

}
