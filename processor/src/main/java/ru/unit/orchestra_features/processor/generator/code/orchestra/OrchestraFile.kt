package ru.unit.orchestra_features.processor.generator.code.orchestra

import com.squareup.kotlinpoet.FileSpec
import ru.unit.orchestra_features.processor.model.FeatureScopeModel

class OrchestraFile {

    fun generate(
        featureScopeModels: List<FeatureScopeModel>,
        packageName: String
    ) = listOf(
        FileSpec.get(
            packageName = packageName,
            typeSpec = OrchestraClass().generate(
                featureScopeModels = featureScopeModels,
                packageName = packageName
            )
        )
    )
}