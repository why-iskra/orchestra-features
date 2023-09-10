package ru.unit.orchestra_features.processor.generator.code.orchestra.property

import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asClassName
import ru.unit.orchestra_features.common.support.Feature
import ru.unit.orchestra_features.common.support.interactive.InteractiveFeatureScope
import ru.unit.orchestra_features.processor.model.FeatureModel
import ru.unit.orchestra_features.processor.model.FeatureScopeModel

class InteractiveScopesProperty {

    companion object {

        fun name() = "interactiveScopes"
    }

    fun generate(featureScopeModels: List<FeatureScopeModel>) = PropertySpec.builder(
        name = name(),
        type = List::class.asClassName()
            .parameterizedBy(InteractiveFeatureScope::class.asClassName())
    ).apply {
        val names = featureScopeModels.filter { model ->
            model.features.any(FeatureModel::interactive)
        }.joinToString(
            transform = ScopeProperty::name
        )

        delegate(
            "%N { %N($names) }",
            "lazy",
            "listOf"
        )
    }.build()
}