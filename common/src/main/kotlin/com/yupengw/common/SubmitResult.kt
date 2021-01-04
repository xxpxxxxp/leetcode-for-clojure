package com.yupengw.common

import java.util.*

class SubmitResult (
    val succeed: Boolean,
    val passedCases: Int,
    val allCases: Int,
    val failedCase: Optional<String>
)