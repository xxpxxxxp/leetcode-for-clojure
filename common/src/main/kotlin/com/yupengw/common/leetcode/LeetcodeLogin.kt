package com.yupengw.common.leetcode

import com.yupengw.common.Difficulty
import com.yupengw.common.SlimQuestion
import com.yupengw.common.SubmitResult
import okhttp3.*
import com.google.gson.Gson

// cross-site request forgery
data class LeetcodeCSRF(
    val username: String,
    val loginCSRF: String,
    val sessionCSRF: String,
    val sessionId: String
)

class LeetcodeSession(session: LeetcodeCSRF) {
    private val baseUrl = "https://leetcode.com"

    fun listProblems(): List<SlimQuestion> {
        data class Stat(
            val question_id: Int,
            val question__title: String,
            val question__title_slug: String,
            val frontend_question_id: Int
        )
        data class Problem(val stat: Stat, val difficulty: Difficulty, val paid_only: Boolean)
        data class ListProblemResult(val stat_status_pairs: List<Problem>)

        return Gson().fromString()
    }

    fun submit(question: SlimQuestion, language: String, code: String): SubmitResult {

    }
}

fun getSetCookieValue(response: Response, key: String): String? =
    response.headers("set-cookie")
        .flatMap { it.split(";") }
        .map { it.trim().split("=") }
        .singleOrNull { it[0] == key }
        ?.get(1)

fun main() {
    val client = OkHttpClient()

    val loginCSRF = client.newCall(
        Request.Builder()
            .url("https://leetcode.com/")
            .build())
        .execute()
        .use { getSetCookieValue(it, "csrftoken") }
        ?: return

    println(loginCSRF)

    client.newCall(
        Request.Builder()
            .url("https://leetcode.com/accounts/login/")
            .header("Origin", "https://leetcode.com")
            .header("Referer", "https://leetcode.com/accounts/login/")
            .header("Cookie", "csrftoken=$loginCSRF;")
            .post(
                MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("csrfmiddlewaretoken", loginCSRF)
                    .addFormDataPart("login", "g04103582@126.com")
                    .addFormDataPart("password", "XP7824147")
                    .build()
            )
            .build())
        .execute()
        .use { response ->
            println(response.code)
            println(getSetCookieValue(response, "csrftoken"))
            println(getSetCookieValue(response, "LEETCODE_SESSION"))
        }
}