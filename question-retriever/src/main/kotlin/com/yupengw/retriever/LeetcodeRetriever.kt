package com.yupengw.retriever

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yupengw.common.SlimQuestion
import com.yupengw.common.leetcode.QuestionMeta
import java.io.File
import java.util.*

fun main(args: Array<String>) {
    val inProgress = mutableSetOf<Int>()
    val backlogQuestions = mutableListOf<Progress>()

    val progressManager = FileQuestionManager(args[0])
    for (progress in progressManager.loadProgress()) {
        if (progress.status != "Accepted")
            backlogQuestions.add(progress)

        inProgress.add(progress.question.questionId)
    }

    val gson = Gson()
    // readin accounts
    val sessions = AccountManager(gson.fromJson(
        File(Thread.currentThread().contextClassLoader.getResource("accounts.key")!!.path).readText(),
        object : TypeToken<List<LeetcodeAccount>>() {}.type
    )
    )

    val candidates: Queue<SlimQuestion> = LinkedList(
        sessions.listQuestions().filter { it.questionId !in inProgress }
    )

    while (candidates.isNotEmpty()) {
        val slim = candidates.poll()
        val progress = Progress(sessions.getFullQuestion(slim.titleSlug), mutableListOf(), "In Progress")
        val meta = gson.fromJson(progress.question.metaData, QuestionMeta::class.java)
        val solver = KotlinSolutionConstructor(meta)
        while (true) {
            // start submitting
            val rst = sessions.submitQuestion(slim, "kotlin", solver.toString())
            progress.status = rst.status
            when (rst.status) {
                "Accepted" -> {
                    // sealed
                    progressManager.saveProgress(progress)
                    break
                }
                "Wrong Answer" -> {
                    if (rst.failedCase.isEmpty)
                        break

                    val case = rst.failedCase.get()
                    solver.add(case.input, case.expectedOutput)
                }
                else -> {
                    // ban it
                    progressManager.saveProgress(progress)
                    backlogQuestions.add(progress)
                    break
                }
            }
        }
    }
}
