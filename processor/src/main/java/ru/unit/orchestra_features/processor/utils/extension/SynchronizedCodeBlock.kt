package ru.unit.orchestra_features.processor.utils.extension

import com.squareup.kotlinpoet.CodeBlock

fun CodeBlock.Companion.synchronizedCodeBlock(
    lock: String,
    vararg args: Any?,
    codeBlock: CodeBlock.Builder.() -> Unit,
) = CodeBlock.builder().apply {
    beginControlFlow("synchronized($lock)", *args)
    this.codeBlock()
    endControlFlow()
}.build()