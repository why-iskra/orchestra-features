package ru.unit.orchestra_features.processor.generator.code.feature.property

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import ru.unit.orchestra_features.processor.model.FeatureModel
import ru.unit.orchestra_features.processor.utils.extension.asClassName

class DataProperty {

    companion object {

        fun name() = "data"
    }

    fun generate(featureModel: FeatureModel) = PropertySpec.builder(
        name = name(),
        type = featureModel.clazz.asClassName(),
        KModifier.PRIVATE
    ).apply {
        mutable(true)
        initializer("%T()", featureModel.clazz.asClassName())
    }.build()
}