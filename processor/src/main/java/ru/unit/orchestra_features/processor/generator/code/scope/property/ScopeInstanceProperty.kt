package ru.unit.orchestra_features.processor.generator.code.scope.property

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import ru.unit.orchestra_features.processor.generator.code.feature.FeatureClass
import ru.unit.orchestra_features.processor.generator.code.orchestra.OrchestraClass
import ru.unit.orchestra_features.processor.generator.code.orchestra.property.ScopeProperty
import ru.unit.orchestra_features.processor.generator.code.scope.ScopeClass
import ru.unit.orchestra_features.processor.model.FeatureModel
import ru.unit.orchestra_features.processor.model.FeatureScopeModel
import ru.unit.orchestra_features.processor.utils.PackageData
import ru.unit.orchestra_features.processor.utils.extension.asClassName

class ScopeInstanceProperty {

    companion object {

        fun name() = "instance"
    }

    fun generate(
        featureScopeModel: FeatureScopeModel,
        packageData: PackageData
    ) = PropertySpec.builder(
        name = name(),
        type = ScopeClass.pack(
            model = featureScopeModel,
            packageData = packageData
        ).asClassName()
    ).apply {
        val scope = "${packageData.name}.${OrchestraClass.name()}.${ScopeProperty.name(featureScopeModel)}"

        getter(
            FunSpec.getterBuilder().apply {
                addCode(
                    "return·$scope"
                )
            }.build()
        )
    }.build()
}