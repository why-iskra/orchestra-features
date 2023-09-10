package ru.unit.orchestra_features.processor.utils.extension

import com.google.devtools.ksp.symbol.KSNode
import ru.unit.orchestra_features.processor.exception.ProcessorException

fun String.checkName(node: KSNode): String {
    val message = when {
        isBlank() -> "Feature scope name is empty"
        any { !(it.isLetterOrDigit() || it == '_') } -> "Feature scope name $this should contain only letters, numbers or '_'"
        get(0).isDigit() -> "Feature scope name $this shouldn't start with number"
        else -> null
    }

    if (message != null) {
        throw ProcessorException(message, node)
    }

    return this
}