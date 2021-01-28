package com.yupengw.retriever.hasher

const val MUL = 269
const val MOD = 1000000007

//++++++++++ Separator ++++++++++
fun hash(str: String, mul: Int = MUL, mod: Int = MOD): Int =
    str.fold(0) { acc, i -> (acc + i.toInt()) * mul % mod }
