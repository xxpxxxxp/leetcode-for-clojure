package com.yupengw.login

interface Retriever {
    fun allQuestions(): List<SlimQuestion>
    fun fullQuestion(question: SlimQuestion): Question
    fun submit(question: SlimQuestion, language: String, solution: String): SubmitResult
}