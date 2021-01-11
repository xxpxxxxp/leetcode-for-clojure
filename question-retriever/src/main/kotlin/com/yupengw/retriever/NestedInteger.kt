package com.yupengw.retriever

/**
 * // This is the interface that allows for creating nested lists.
 * // You should not implement it, or speculate about its implementation*/
class NestedInteger {
    var int: Int? = null
    var inner: MutableList<NestedInteger>? = null
    var isInt: Boolean = false

    // Constructor initializes an empty nested list.
    constructor()

    // Constructor initializes a single integer.
    constructor(value: Int)

    // @return true if this NestedInteger holds a single integer, rather than a nested list.
    fun isInteger(): Boolean = isInt

    // @return the single integer that this NestedInteger holds, if it holds a single integer
    // Return null if this NestedInteger holds a nested list
    fun getInteger(): Int? = int

    // Set this NestedInteger to hold a single integer.
    fun setInteger(value: Int): Unit {
        isInt = true
        int = value
    }

    // Set this NestedInteger to hold a nested list and adds a nested integer to it.
    fun add(ni: NestedInteger): Unit {
        if (inner == null) {
            inner = mutableListOf()
        }
        inner!!.add(ni)
        isInt = false
    }

    // @return the nested list that this NestedInteger holds, if it holds a nested list
    // Return null if this NestedInteger holds a single integer
    fun getList(): List<NestedInteger>? = inner

    override fun toString(): String {
        return "Is int: $isInt\nInt val: $int\nList val: $inner\n"
    }
}