package ru.unit.orchestra_features.processor.generator.code.scope.property

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asClassName
import ru.unit.orchestra_features.common.annotation.Source
import ru.unit.orchestra_features.common.support.interactive.ElementData
import ru.unit.orchestra_features.common.support.interactive.InteractiveFeatureData
import ru.unit.orchestra_features.common.support.interactive.InteractiveScopeData
import ru.unit.orchestra_features.processor.exception.ProcessorException
import ru.unit.orchestra_features.processor.model.FeatureModel
import ru.unit.orchestra_features.processor.model.FeatureScopeModel

class InteractiveScopeProperty {

    companion object {

        fun name() = "__generated_scope"
    }

    fun generate(featureScopeModel: FeatureScopeModel) = PropertySpec.builder(
        name = name(),
        type = InteractiveScopeData::class.asClassName(),
        KModifier.OVERRIDE
    ).apply {
        val code = CodeBlock.builder().apply {
            beginControlFlow("lazy")

            scopeModelToCode(featureScopeModel)?.also(::add) ?: throw ProcessorException(
                message = "Scope doesn't have interactive features",
                node = featureScopeModel.ksNode
            )

            endControlFlow()
        }.build()

        delegate(code)
    }.build()

    private fun scopeModelToCode(featureScopeModel: FeatureScopeModel) =
        if (featureScopeModel.features.none(FeatureModel::interactive)) {
            null
        } else {
            CodeBlock.builder().apply {
                add("%T(", InteractiveScopeData::class.asClassName())

                add("%N·=·", "element")
                add(featureScopeModel.toElement())

                add(",·%N·=·%N(", "dependsOn", "listOf")
                featureScopeModel.dependsOn.forEach { model ->
                    add(model.toElement())
                    add(",")
                }
                add(")")

                add(",·%N·=·%N(", "features", "listOf")
                featureScopeModel.features
                    .mapNotNull(::featureModelToCode)
                    .forEach { code ->
                        add(code)
                        add(",")
                    }
                add(")")

                add(")")
            }.build()
        }

    private fun featureModelToCode(featureModel: FeatureModel) =
        if (featureModel.interactive) {
            CodeBlock.builder().apply {
                add("%T(", InteractiveFeatureData::class.asClassName())

                add("%N·=·", "element")
                add(featureModel.toElement())

                add(",·%N·=·%S", "description", featureModel.description ?: "")

                add(",·%N·=·%N(", "dependsOn", "listOf")
                featureModel.dependsOn.forEach { model ->
                    add(model.toElement())
                    add(",")
                }
                add(")")

                add(
                    format = ",·%N·=·%N", "feature",
                    FeatureProperty.name(featureModel)
                )

                if (featureModel.toggleable.source.contains(Source.INTERACTIVE)) {
                    add(
                        format = ",·%N·=·%N", "toggleable",
                        FeatureProperty.name(featureModel)
                    )
                } else {
                    add(",·%N·=·null", "toggleable")
                }

                add(")")
            }.build()
        } else {
            null
        }

    private fun FeatureScopeModel.toElement() = CodeBlock.builder().apply {
        add(
            format = "%T(%N·=·%S,·%N·=·%S)",
            ElementData::class.asClassName(),
            "name",
            name,
            "id",
            id
        )
    }.build()

    private fun FeatureModel.toElement() = CodeBlock.builder().apply {
        add(
            format = "%T(%N·=·%S,·%N·=·%S)",
            ElementData::class.asClassName(),
            "name",
            name,
            "id",
            id
        )
    }.build()
}