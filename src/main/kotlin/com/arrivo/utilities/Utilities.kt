package com.arrivo.utilities

import java.util.*

fun generateRandomPassword(length: Int): String {
    val charset = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { charset.random() }
        .joinToString("")
}

fun capitalize(str: String): String {
    return str.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}