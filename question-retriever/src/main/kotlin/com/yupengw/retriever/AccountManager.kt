package com.yupengw.retriever

import com.yupengw.common.Question
import com.yupengw.common.QuestionRetriever
import com.yupengw.common.SlimQuestion
import com.yupengw.common.SubmitResult
import com.yupengw.common.leetcode.LeetcodeSession

data class LeetcodeAccount(val id: String, val csrf: String)

class AccountManager(accounts: List<LeetcodeAccount>): QuestionRetriever {
    init {
        require(accounts.isNotEmpty())
    }
    private val sessions = accounts.map { LeetcodeSession(it.csrf, it.id) }.toMutableList()
    private var lastIdx = -1
    private var lastSubmitTime = -1L

    override fun listQuestions(): List<SlimQuestion> = sessions.random().listQuestions()

    override fun getFullQuestion(titleSlug: String): Question = sessions.random().getFullQuestion(titleSlug)

    override fun submitQuestion(question: SlimQuestion, language: String, solution: String): SubmitResult {
        // round robin, throttling
        val throttle = 10000L + lastSubmitTime - System.currentTimeMillis()
        if (throttle > 0) {
            Thread.sleep(throttle)
        }

        var idx = lastIdx+1
        while (sessions.isNotEmpty()) {
            if (idx >= sessions.size)
                idx = 0

            try {
                val rst = sessions[idx].submitQuestion(question, language, solution)
                lastIdx = idx
                lastSubmitTime = System.currentTimeMillis()
                return rst
            } catch (e: IllegalAccessException) {
                // remove invalid account, try next one
                sessions.removeAt(idx)
            }
        }

        throw Exception("no account available")
    }
}