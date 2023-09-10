package ru.unit.orchestra_features.processor.generator.code.feature.property

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import ru.unit.orchestra_features.processor.model.FeatureModel

class ManualToggleProperty {

    companion object {

        fun name() = "manualToggle"
    }

    fun generate(featureModel: FeatureModel) = PropertySpec.builder(
        name = name(),
        type = Boolean::class.java,
        KModifier.PRIVATE
    ).apply {
        mutable(true)
        initializer(featureModel.toggleable.defaultValue.toString())
    }.build()
}