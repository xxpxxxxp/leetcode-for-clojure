package com.yupengw.login.leetcode

import com.yupengw.login.Question
import com.yupengw.login.Retriever
import com.yupengw.login.SlimQuestion
import com.yupengw.login.SubmitResult

class LeetcodeRetriever(val username: String, val password: String): Retriever, AutoCloseable {
    private var logined: Boolean = false

    override fun allQuestions(): List<SlimQuestion> {
        TODO("Not yet implemented")
    }

    override fun fullQuestion(question: SlimQuestion): Question {
        TODO("Not yet implemented")
    }

    override fun submit(question: SlimQuestion, language: String, solution: String): SubmitResult {
        TODO("Not yet implemented")
    }

    override fun close() {
        if (logined) {
            logout()
        }
    }

    private fun login() {

    }

    private fun logout() {

    }
}