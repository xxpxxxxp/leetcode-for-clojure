package com.yupengw.common

import java.util.Optional

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