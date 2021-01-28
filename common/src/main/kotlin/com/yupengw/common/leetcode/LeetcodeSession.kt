package com.yupengw.common.leetcode

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.yupengw.common.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*
import java.util.concurrent.TimeoutException

open class LeetcodeAnonymous : QuestionRetriever {
    companion object {
        const val JSON_TYPE = "application/json"
        val DIFFICULTY_MAP = mapOf("Easy" to 1, "Medium" to 2, "Hard" to 3)
    }

    protected val client = OkHttpClient()
    protected val gson = Gson()

    protected open fun buildRequest(url: String): Request.Builder =
        Request.Builder()
            .url(url)
            .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36 Edg/87.0.664.66")
            .header("Accept", JSON_TYPE)
            .header("Accept-Language", "en-US,en;q=0.5")
            .header("Origin", "https://leetcode.com")
            .header("Referer", url)

    data class Stat(
        val question_id: Int,
        val question__title: String,
        val question__title_slug: String,
        val frontend_question_id: Int
    )

    data class Difficulty(val level: Int)
    data class Problem(val stat: Stat, val difficulty: Difficulty, val paid_only: Boolean)
    data class ListProblemResult(val stat_status_pairs: List<Problem>)

    override fun listQuestions(): List<SlimQuestion> {
        client.newCall(buildRequest("https://leetcode.com/api/problems/all/").build())
            .execute()
            .use { response ->
                if (response.code == 200) {
                    return Gson().fromJson(response.body?.string(), ListProblemResult::class.java)
                        .stat_status_pairs
                        .filter { !it.paid_only }
                        .map {
                            SlimQuestion(
                                Source.Leetcode,
                                it.stat.question_id,
                                it.stat.frontend_question_id,
                                it.stat.question__title,
                                it.stat.question__title_slug,
                                it.difficulty.level
                            )
                        }
                }
            }

        throw Exception("failed to get all questions!")
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
                            it.metaData
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
) : LeetcodeAnonymous() {

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

        return client.newCall(
            buildRequest("https://leetcode.com/problems/${question.titleSlug}/submit/")
                .post(body.toRequestBody(JSON_TYPE.toMediaType()))
                .build())
            .execute()
            .use { response ->
                when (response.code) {
                    200 ->
                        checkResult(gson.fromJson(response.body?.string(), LeetcodeSubmission::class.java).submission_id).let {
                            SubmitResult(
                                it.status_msg!!,
                                it.total_correct!!,
                                it.total_testcases!!,
                                Optional.of(TestCase(it.last_testcase!!, it.expected_output!!))
                            )
                        }
                    403 ->
                        throw IllegalAccessException("access denied")
                    else ->
                        throw Exception("failed to submit question: ${response.code}")
                }
            }
    }
}

data class ReturnParam(val type: String)
data class Param(val name: String, val type: String)
data class QuestionMeta(val name: String, val params: List<Param>, @SerializedName("return") val returnParam: ReturnParam)

fun main() {
    try {
        val gson = Gson()
        val r = LeetcodeAnonymous()
        val p = mutableSetOf<String>()
        for (q in r.listQuestions()) {
            try {
                val fq = r.getFullQuestion(q.titleSlug)
                Thread.sleep(1000L)
                val meta = gson.fromJson(fq.metaData, QuestionMeta::class.java)
                p.addAll(meta.params.map { it.type })
                p.add(meta.returnParam.type)
            } catch (e: Exception) {
                // ignore
            }
        }

        println(p.toList())
    } catch (e: Exception) {
        println(e)
    }
}