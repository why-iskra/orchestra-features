package ru.unit.orchestra_features.processor.generator.code.orchestra.property

import com.squareup.kotlinpoet.PropertySpec
import ru.unit.orchestra_features.processor.generator.code.scope.ScopeClass
import ru.unit.orchestra_features.processor.model.FeatureScopeModel
import ru.unit.orchestra_features.processor.utils.PackageData
import ru.unit.orchestra_features.processor.utils.extension.asClassName
import java.util.*

class ScopeProperty {

    companion object {

        fun name(featureScopeModel: FeatureScopeModel) =
            featureScopeModel.name.replaceFirstChar { c -> c.lowercase(Locale.getDefault()) }
    }

    fun generate(
        featureScopeModel: FeatureScopeModel,
        packageData: PackageData
    ) = PropertySpec.builder(
        name = name(featureScopeModel),
        type = ScopeClass.pack(
            model = featureScopeModel,
            packageData = packageData
        ).asClassName()
    ).apply {
        val className = ScopeClass.pack(
            model = featureScopeModel,
            packageData = packageData
        ).asClassName()

        delegate("lazy·{·%T()·}", className)
    }.build()
}