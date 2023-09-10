package ru.unit.orchestra_features.common.utils.delegate

fun Any?.dataClassBeautifier(): String {
    val indentSize = 2

    val string = toString()
    var level = 0
    var i = 0

    fun tab() = " ".repeat(indentSize * level)

    val output = buildString {
        while (i < string.length) {
            when (val c = string[i]) {
                '[', '{', '(' -> {
                    level++
                    append("$c\n${tab()}")
                }

                ']', '}', ')' -> {
                    level--
                    append("\n${tab()}$c")
                }

                '\n' -> append("\\n")
                '\t' -> append("\\t")
                '\r' -> append("\\r")

                ',' -> {
                    append("$c\n${tab()}")

                    while (string[i + 1] == ' ') i++
                }

                else -> append(c)
            }

            i++
        }
    }.trim()

    return output
}