package ru.unit.orchestra_features.processor.generator.code.orchestra.property

import com.squareup.kotlinpoet.PropertySpec
import ru.unit.orchestra_features.processor.generator.code.scope.ScopeClass
import ru.unit.orchestra_features.processor.model.FeatureScopeModel
import ru.unit.orchestra_features.processor.utils.extension.asClassName
import java.util.*

class ScopeProperty {

    companion object {

        fun name(featureScopeModel: FeatureScopeModel) =
            featureScopeModel.name.replaceFirstChar { c -> c.lowercase(Locale.getDefault()) }
    }

    fun generate(
        featureScopeModel: FeatureScopeModel,
        packageName: String
    ) = PropertySpec.builder(
        name = name(featureScopeModel),
        type = ScopeClass.pack(
            model = featureScopeModel,
            packageName = packageName
        ).asClassName()
    ).apply {
        val className = ScopeClass.pack(
            model = featureScopeModel,
            packageName = packageName
        ).asClassName()

        delegate("lazy { %T() }", className)
    }.build()
}