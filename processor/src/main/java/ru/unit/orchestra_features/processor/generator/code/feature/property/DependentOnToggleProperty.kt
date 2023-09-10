package ru.unit.orchestra_features.processor.generator.code.feature.property

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import ru.unit.orchestra_features.processor.model.FeatureModel
import ru.unit.orchestra_features.processor.utils.extension.synchronizedCodeBlock

class DependentOnToggleProperty {

    companion object {

        fun name(featureModel: FeatureModel) = "dependentOn${featureModel.id}Toggle"
    }

    fun generate(featureModel: FeatureModel) = PropertySpec.builder(
        name = name(featureModel),
        type = Boolean::class.java,
        KModifier.INTERNAL
    ).apply {
        mutable(true)
        initializer("false")

        setter(
            FunSpec.setterBuilder().apply {
                addParameter("value", Boolean::class.java)
                addCode(
                    CodeBlock.synchronizedCodeBlock("this") {
                        addStatement("field = value")
                    }
                )
            }.build()
        )
    }.build()
}