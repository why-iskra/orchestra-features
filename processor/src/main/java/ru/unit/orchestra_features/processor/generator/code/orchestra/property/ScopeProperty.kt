package ru.unit.orchestra_features.processor.generator.code.orchestra.property

import com.squareup.kotlinpoet.PropertySpec
import ru.unit.orchestra_features.processor.generator.code.orchestra.ScopeClass
import ru.unit.orchestra_features.processor.model.FeatureScopeModel
import ru.unit.orchestra_features.processor.utils.extension.asClassName

class ScopeProperty {

    companion object {

        fun name(featureScopeModel: FeatureScopeModel) = featureScopeModel.name
    }

    fun generate(featureScopeModel: FeatureScopeModel) = PropertySpec.builder(
        name = name(featureScopeModel),
        type = ScopeClass.name(featureScopeModel).asClassName()
    ).apply {
        val className = ScopeClass.name(featureScopeModel).asClassName()

        delegate("lazy { %T() }", className)
    }.build()
}