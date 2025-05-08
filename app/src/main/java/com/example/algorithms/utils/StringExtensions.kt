package com.example.algorithms.utils

fun String.toFibName(): String {
    val number = this.filter { it.isDigit() }.ifEmpty { "?" }
    return "fib($number)"
}