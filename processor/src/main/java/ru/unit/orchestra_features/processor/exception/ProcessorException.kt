package ru.unit.orchestra_features.processor.exception

import com.google.devtools.ksp.symbol.KSNode

class ProcessorException(
    override val message: String,
    val node: KSNode,
    val additionalMessages: List<String> = emptyList()
) : Throwable()
