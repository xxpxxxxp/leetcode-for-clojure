package com.yupengw.login

open class SlimQuestion(
    val source: Source,
    val id: Int,
    val title: String,
    val link: String
)

open class Question (
    source: Source,
    id: Int,
    title: String,
    link: String,
    val description: String,
    val ktSolution: String,
    val difficulty: Difficulty,
    val tag: Set<String>
): SlimQuestion(source, id, title, link)

class StoutQuestion (
    source: Source,
    id: Int,
    title: String,
    link: String,
    description: String,
    ktSolution: String,
    difficulty: Difficulty,
    tag: Set<String>,
    val testCases: Set<String>
): Question(source, id, title, link, description, ktSolution, difficulty, tag)