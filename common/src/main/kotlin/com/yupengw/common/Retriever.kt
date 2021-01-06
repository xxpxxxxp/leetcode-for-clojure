package com.yupengw.common

interface QuestionRetriever {
    fun listQuestions(): List<SlimQuestion>
    fun getFullQuestion(titleSlug: String): Question
    fun submitQuestion(question: SlimQuestion, language: String, solution: String): SubmitResult
}