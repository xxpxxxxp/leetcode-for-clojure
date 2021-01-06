package com.yupengw.common.leetcode

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.yupengw.common.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*
import java.util.concurrent.TimeoutException

open class LeetcodeRetriever : QuestionRetriever {
    companion object {
        const val JSON_TYPE = "application/json"
        val DIFFICULTY_MAP = mapOf("Easy" to 1, "Medium" to 2, "Hard" to 3)
    }

    protected val client = OkHttpClient()
    protected val gson = Gson()

    protected open fun buildRequest(url: String): Request.Builder =
        Request.Builder()
            .url(url)
            .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36")
            .header("Accept", JSON_TYPE)
            .header("Accept-Language", "en-US,en;q=0.5")
            .header("Origin", "https://leetcode.com")
            .header("Referer", url)

    override fun listQuestions(): List<SlimQuestion> {
        TODO("Not yet implemented")
    }

    internal data class LeetcodeQuestion(
        val questionId: String,
        val questionFrontendId: String,
        val title: String,
        val titleSlug: String,
        val difficulty: String,
        val content: String,
        val similarQuestions: String?,
        val topicTags: List<TopicTag>,
        val codeSnippets: List<CodeSnippet>,
        val hints: List<String>,
        val sampleTestCase: String,
        val metaData: String
    )
    internal data class LeetcodeData(val question: LeetcodeQuestion)
    internal data class LeetcodeQueryResult(val data: LeetcodeData)

    override fun getFullQuestion(titleSlug: String): Question {
        val body = JsonObject().apply {
            this.addProperty("operationName", "questionData")
            this.add("variables", JsonObject().apply {
                this.addProperty("titleSlug", titleSlug)
            })
            this.addProperty("query", """
                query questionData(${"$"}titleSlug : String!) {
                    question(titleSlug: ${"$"}titleSlug) {
                        questionId
                        questionFrontendId
                        boundTopicId
                        title
                        titleSlug
                        content
                        translatedTitle
                        translatedContent
                        isPaidOnly
                        difficulty
                        likes
                        dislikes
                        isLiked
                        similarQuestions
                        contributors {
                            username
                            profileUrl
                            avatarUrl
                            __typename
                        }
                        langToValidPlayground
                        topicTags {
                            name
                            slug
                            translatedName
                            __typename
                        }
                        companyTagStats
                        codeSnippets {
                            lang
                            langSlug
                            code
                            __typename
                        }
                        stats
                        hints
                        solution {
                            id
                            canSeeDetail
                            __typename
                        }
                        status
                        sampleTestCase
                        metaData
                        judgerAvailable
                        judgeType
                        mysqlSchemas
                        enableRunCode
                        enableTestMode
                        envInfo
                        __typename
                  }
                }
            """.trimIndent()
            )
        }.toString()

        client.newCall(
            buildRequest("https://leetcode.com/graphql")
                .post(body.toRequestBody(JSON_TYPE.toMediaType()))
                .build())
            .execute()
            .use { response ->
                if (response.code == 200) {
                    return gson.fromJson(response.body?.string(), LeetcodeQueryResult::class.java).data.question.let {
                        Question(
                            Source.Leetcode,
                            it.questionId.toInt(),
                            it.questionFrontendId.toInt(),
                            it.title,
                            it.titleSlug,
                            DIFFICULTY_MAP[it.difficulty]!!,
                            it.content,
                            it.hints,
                            it.sampleTestCase,
                            gson.fromJson(it.similarQuestions, object : TypeToken<List<BaseQuestion>>() {}.type),
                            it.topicTags,
                            it.metaData,
                            it.codeSnippets.singleOrNull { it.lang == "Kotlin" }?.code
                        )
                    }
                }
            }

        throw Exception("failed to get full question!")
    }

    override fun submitQuestion(question: SlimQuestion, language: String, solution: String): SubmitResult {
        TODO("Not yet implemented")
    }
}

class LeetcodeSession(
    // cross-site request forgery
    private val sessionCSRF: String,
    private val sessionId: String
) : LeetcodeRetriever() {

    override fun buildRequest(url: String): Request.Builder =
        super.buildRequest(url)
            .header("X-CSRFToken", sessionCSRF)
            .header(
                "Cookie",
                listOf(
                    "LEETCODE_SESSION" to sessionId,
                    "csrftoken" to sessionCSRF
                ).joinToString(";") { "${it.first}=${it.second}" }
            )

    internal data class LeetcodeSubmissionResult(
        val state: String,
        val status_code: Int?,
        val status_msg: String?,
        val run_success: Boolean?,
        val last_testcase: String?,
        val expected_output: String?,
        val total_correct: Int?,
        val total_testcases: Int?,
//        val submission_id: String?,
//        val question_id: Int?,
//        val lang: String?,
//        val pretty_lang: String?,
//        val status_runtime: String?,
//        val status_memory: String?,
//        val runtime_percentile: Double?,
//        val memory_percentile: Double?,
//        val elapsed_time: Int?,
//        val memory: Int?,
//        val display_runtime: String?,
//        val compare_result: String?,
//        val input_formatted: String?,
//        val input: String?,
//        val code_output: String?,
//        val std_output: String?,
//        val task_finish_time: Long?
    )

    private fun checkResult(submitId: Long): LeetcodeSubmissionResult {
        repeat(60) {
            Thread.sleep(1000L)

            client.newCall(
                buildRequest("https://leetcode.com/submissions/detail/$submitId/check/")
                    .get()
                    .build())
                .execute()
                .use { response ->
                    if (response.code == 200) {
                        val rst = gson.fromJson(response.body?.string(), LeetcodeSubmissionResult::class.java)
                        if (rst.state == "SUCCESS")
                            return rst
                    }
                }
        }

        throw TimeoutException("fetch submission timeout!")
    }

    internal data class LeetcodeSubmission(val submission_id: Long)

    override fun submitQuestion(question: SlimQuestion, language: String, solution: String): SubmitResult {
        val body = JsonObject().apply {
            this.addProperty("question_id", question.questionId)
            this.addProperty("lang", language)
            this.addProperty("typed_code", solution)
        }.toString()

        client.newCall(
            buildRequest("https://leetcode.com/problems/${question.titleSlug}/submit/")
                .post(body.toRequestBody(JSON_TYPE.toMediaType()))
                .build())
            .execute()
            .use { response ->
                if (response.code == 200) {
                    return checkResult(gson.fromJson(response.body?.string(), LeetcodeSubmission::class.java).submission_id).let {
                        SubmitResult(
                            it.status_msg!!,
                            it.total_correct!!,
                            it.total_testcases!!,
                            Optional.of(TestCase(it.last_testcase!!, it.expected_output!!)
                            ))
                    }
                }
            }

        throw Exception("failed to submit question!")
    }
}

fun main() {
    try {
        val q = LeetcodeRetriever().getFullQuestion("dinner-plate-stacks")
        println(q)
    } catch (e: Exception) {
        // ignore
    }
}