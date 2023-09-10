package ru.unit.orchestra_features.processor.generator.code.scope.property

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import ru.unit.orchestra_features.processor.generator.code.orchestra.OrchestraClass
import ru.unit.orchestra_features.processor.generator.code.orchestra.property.ScopeProperty
import ru.unit.orchestra_features.processor.generator.code.scope.ScopeClass
import ru.unit.orchestra_features.processor.generator.code.scope.ScopeFile
import ru.unit.orchestra_features.processor.model.FeatureScopeModel
import ru.unit.orchestra_features.processor.utils.extension.asClassName

class FeaturesProperty {

    companion object {

        fun name() = "features"
    }

    fun generate(
        featureScopeModel: FeatureScopeModel,
        packageName: String
    ) = PropertySpec.builder(
        name = name(),
        type = ScopeClass.pack(
            model = featureScopeModel,
            packageName = packageName
        ).asClassName()
    ).apply {
        getter(
            FunSpec.getterBuilder().apply {
                addCode("return $packageName.${OrchestraClass.name()}.${ScopeProperty.name(featureScopeModel)}")
            }.build()
        )
    }.build()
}