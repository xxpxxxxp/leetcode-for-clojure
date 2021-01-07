package com.yupengw.common

import java.util.*

enum class Source {
    Leetcode
}

open class BaseQuestion(
    val title: String,
    val titleSlug: String
)

open class SlimQuestion(
    val source: Source,
    val questionId: Int,
    val frontendId: Int,
    title: String,
    titleSlug: String,
    val difficulty: Int
): BaseQuestion(title, titleSlug) {
    override fun toString(): String = "$source - $frontendId. $title"
}

data class TopicTag(val name: String, val slug: String)
data class CodeSnippet(val lang: String, val langSlug: String, val code: String)

open class Question (
    source: Source,
    questionId: Int,
    frontendId: Int,
    title: String,
    titleSlug: String,
    difficulty: Int,
    val description: String,
    val hints: List<String>,
    val sampleTestCase: String,
    val similarQuestions: List<BaseQuestion>,
    val topicTags: List<TopicTag>,
    val metaData: String,
    val ktSolution: String?
): SlimQuestion(source, questionId, frontendId, title, titleSlug, difficulty)

data class TestCase(val case: String, val expectedOutput: String)

class FullQuestion (
    source: Source,
    questionId: Int,
    frontendId: Int,
    title: String,
    titleSlug: String,
    difficulty: Int,
    description: String,
    hints: List<String>,
    sampleTestCase: String,
    similarQuestions: List<BaseQuestion>,
    topicTags: List<TopicTag>,
    metaData: String,
    ktSolution: String,
    val testCases: List<TestCase>
): Question(source, questionId, frontendId, title, titleSlug, difficulty, description, hints, sampleTestCase, similarQuestions, topicTags, metaData, ktSolution)

enum class CallType {
    UNIFORM,
    SEQUENCE
}

class ParamType(
    val `type`: String,
    val innerType: Optional<ParamType>
)

open class NamedSignature(val name: String)

class ParamSignature(
    name: String,
    val type: ParamType
): NamedSignature(name)

class MethodSignature(
    name: String,
    val params: List<ParamSignature>,
    val returnParams: List<ParamSignature>
): NamedSignature(name)

class QuestionSignature(
    val callType: CallType,
    name: String,
    val methods: List<MethodSignature>
): NamedSignature(name)

data class SubmitResult (
    val status: String,
    val passedCases: Int = 0,
    val allCases: Int = 0,
    val failedCase: Optional<TestCase> = Optional.empty()
)
