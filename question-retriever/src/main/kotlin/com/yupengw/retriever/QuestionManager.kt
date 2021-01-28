package com.yupengw.retriever

import com.google.gson.Gson
import com.yupengw.common.Question
import com.yupengw.common.TestCase
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

data class Progress(
    val question: Question,
    val testcases: MutableList<TestCase>,
    var status: String
)

interface QuestionManager {
    fun saveProgress(progress: Progress)
    fun loadProgress(): List<Progress>
}

class FileQuestionManager(private val workingDir: String): QuestionManager {
    init {
        Files.createDirectories(Paths.get(workingDir))
    }

    private val gson = Gson()

    override fun saveProgress(progress: Progress) {
        val file = Paths.get(workingDir, progress.question.titleSlug).toFile()
        file.writeText(gson.toJson(progress))
    }

    override fun loadProgress(): List<Progress> =
        Files.list(Paths.get(workingDir))
            .filter { Files.isReadable(it) }
            .map {
                try {
                    gson.fromJson(Files.readString(it), Progress::class.java)
                } catch (e: Exception) {
                    null
                }
            }
            .filter { it != null }
            .map { it!! }
            .toList()
}