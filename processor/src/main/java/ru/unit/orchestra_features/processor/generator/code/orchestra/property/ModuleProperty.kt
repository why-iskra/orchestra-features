package ru.unit.orchestra_features.processor.generator.code.orchestra.property

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import ru.unit.orchestra_features.processor.FeatureAnnotationProcessor

class ModuleProperty {

    companion object {

        fun name() = "__generated_module"
    }

    fun generate(packageName: String) = PropertySpec.builder(
        name = name(),
        type = String::class,
        KModifier.OVERRIDE
    ).apply {
        initializer("%S", packageName.removeSuffix(FeatureAnnotationProcessor.PACKAGE_SUFFIX))
    }.build()
}