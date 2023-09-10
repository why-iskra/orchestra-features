package ru.unit.orchestra_features.processor.generator.code.orchestra.property

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asClassName
import ru.unit.orchestra_features.common.annotation.Source
import ru.unit.orchestra_features.processor.model.FeatureModel
import ru.unit.orchestra_features.processor.model.FeatureScopeModel
import ru.unit.orchestra_features.common.support.interactive.ElementData
import ru.unit.orchestra_features.common.support.interactive.InteractiveFeatureData
import ru.unit.orchestra_features.common.support.interactive.InteractiveScopeData

class InteractiveScopesProperty {

    companion object {

        fun name() = "__generated_scopes"
    }

    fun generate(featureScopeModels: List<FeatureScopeModel>) = PropertySpec.builder(
        name = name(),
        type = List::class.asClassName()
            .parameterizedBy(InteractiveScopeData::class.asClassName()),
        KModifier.OVERRIDE
    ).apply {
        val scopeClasses = featureScopeModels.mapNotNull(::scopeModelToCode)

        initializer(
            CodeBlock.builder().apply {
                add("%N(", "listOf")

                scopeClasses.forEach { code ->
                    add(code)
                    add(",")
                }

                add(")")
            }.build()
        )
    }.build()

    private fun scopeModelToCode(featureScopeModel: FeatureScopeModel) =
        if (featureScopeModel.features.none(FeatureModel::interactive)) {
            null
        } else {
            CodeBlock.builder().apply {
                add("%T(", InteractiveScopeData::class.asClassName())

                add("%N = ", "element")
                add(featureScopeModel.toElement())

                add(", %N = %N(", "dependsOn", "listOf")
                featureScopeModel.dependsOn.forEach { model ->
                    add(model.toElement())
                    add(",")
                }
                add(")")

                add(", %N = %N(", "features", "listOf")
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

                add("%N = ", "element")
                add(featureModel.toElement())

                add(", %N = %S", "description", featureModel.description ?: "")

                add(", %N = %N(", "dependsOn", "listOf")
                featureModel.dependsOn.forEach { model ->
                    add(model.toElement())
                    add(",")
                }
                add(")")

                add(
                    format = ", %N = %N.%N", "feature",
                    ScopeProperty.name(featureModel.scope),
                    FeatureScopeProperty.name(featureModel)
                )

                if (featureModel.toggleable.source.contains(Source.INTERACTIVE)) {
                    add(
                        format = ", %N = %N.%N", "toggleable",
                        ScopeProperty.name(featureModel.scope),
                        FeatureScopeProperty.name(featureModel)
                    )
                } else {
                    add(", %N = null", "toggleable")
                }

                add(")")
            }.build()
        } else {
            null
        }

    private fun FeatureScopeModel.toElement() = CodeBlock.builder().apply {
        add(
            format = "%T(%N = %S, %N = %S)",
            ElementData::class.asClassName(),
            "name",
            name,
            "id",
            id
        )
    }.build()

    private fun FeatureModel.toElement() = CodeBlock.builder().apply {
        add(
            format = "%T(%N = %S, %N = %S)",
            ElementData::class.asClassName(),
            "name",
            name,
            "id",
            id
        )
    }.build()
}