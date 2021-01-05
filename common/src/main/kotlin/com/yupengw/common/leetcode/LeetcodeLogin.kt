package com.yupengw.common.leetcode

import com.yupengw.common.SlimQuestion
import okhttp3.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class LeetcodeSession(
    // cross-site request forgery
    var sessionCSRF: String,
    val sessionId: String
) {
    private val baseUrl = "https://leetcode.com"
    private val client = OkHttpClient()
    private val gson = Gson()

    private fun buildRequest(url: String): Request.Builder =
        Request.Builder()
            .url(url)
            .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36")
            .header("Accept", "application/json")
            .header("Accept-Language", "en-US,en;q=0.5")
            .header("Origin", "https://leetcode.com")
            .header("Referer", url)
            .header("X-CSRFToken", sessionCSRF)
            .header(
                "Cookie",
                listOf(
                    "LEETCODE_SESSION" to sessionId,
                    "csrftoken" to sessionCSRF
                ).joinToString(";") { "${it.first}=${it.second}" }
            )

    internal data class Submission(val submission_id: Long)

    fun checkResult(submitId: Long) {
        client.newCall(
            buildRequest("https://leetcode.com/submissions/detail/$submitId/check/")
                .get()
                .build())
            .execute()
            .use { r2 ->
                println(r2.code)
                println(r2.body?.string())
            }
    }

    fun submit(question: SlimQuestion, language: String, code: String) {
        val body = JsonObject().apply {
            this.addProperty("question_id", question.id)
            this.addProperty("lang", language)
            this.addProperty("typed_code", code)
        }.toString()

        client.newCall(
            buildRequest(question.link + "/submit/")
                .post(
                    body.toRequestBody("application/json".toMediaType())
                )
                .build())
            .execute()
            .use { r1 ->
                if (r1.code == 200) {
                    val submitId = gson.fromJson(r1.body?.string(), Submission::class.java).submission_id
                    client.newCall(
                        buildRequest("https://leetcode.com/submissions/detail/$submitId/check/")
                            .get()
                            .build())
                        .execute()
                        .use { r2 ->
                            println(r2.code)
                            println(r2.body?.string())
                        }
                }
            }
    }
}

fun main() {
}