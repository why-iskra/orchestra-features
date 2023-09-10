package ru.unit.orchestra_features.processor.generator.code.feature.function

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import ru.unit.orchestra_features.processor.generator.code.feature.property.ManualToggleProperty
import ru.unit.orchestra_features.processor.utils.extension.synchronizedCodeBlock

class InteractiveToggleFunction {

    companion object {

        fun name() = "__interactiveToggle"
    }

    fun generate() = FunSpec.builder(name()).apply {
        addModifiers(KModifier.OVERRIDE)

        addParameter(
            name = "value",
            type = Boolean::class
        )

        addCode(
            CodeBlock.synchronizedCodeBlock("this") {
                addStatement("${ManualToggleProperty.name()} = value")
                addStatement("${UpdateFunction.name()}()")
            }
        )
    }.build()
}