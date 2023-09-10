package ru.unit.orchestra_features.processor.generator.code.feature.function

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.asClassName
import ru.unit.orchestra_features.processor.generator.code.feature.property.DataProperty
import ru.unit.orchestra_features.processor.generator.code.feature.property.ManualToggleProperty
import ru.unit.orchestra_features.processor.model.FeatureModel
import ru.unit.orchestra_features.processor.utils.extension.asClassName
import ru.unit.orchestra_features.processor.utils.extension.synchronizedCodeBlock

class ToggleFunction {

    companion object {

        fun name() = "toggle"
    }

    fun generate(featureModel: FeatureModel) = FunSpec.builder(name()).apply {
        val isMutable = featureModel.mutable

        addParameter(
            ParameterSpec.builder(
                name = "value",
                type = Boolean::class.asClassName().copy(nullable = true)
            ).apply {
                defaultValue("null")
            }.build()
        )

        if (isMutable) {
            addParameter(
                ParameterSpec.builder(
                    name = "data",
                    type = featureModel.clazz.asClassName().copy(nullable = true)
                ).apply {
                    defaultValue("null")
                }.build()
            )
        }

        addCode(
            CodeBlock.synchronizedCodeBlock("this") {
                beginControlFlow("if (value != null)")
                addStatement("${ManualToggleProperty.name()} = value")
                endControlFlow()

                if (isMutable) {
                    beginControlFlow("if (data != null)")
                    addStatement("this.${DataProperty.name()} = data")
                    endControlFlow()
                }

                addStatement("${UpdateFunction.name()}()")
            }
        )
    }.build()
}