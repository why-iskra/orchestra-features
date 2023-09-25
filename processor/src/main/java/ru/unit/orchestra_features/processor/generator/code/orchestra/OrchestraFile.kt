package ru.unit.orchestra_features.processor.generator.code.orchestra

import com.squareup.kotlinpoet.FileSpec
import ru.unit.orchestra_features.processor.model.FeatureScopeModel
import ru.unit.orchestra_features.processor.utils.PackageData

class OrchestraFile {

    fun generate(
        featureScopeModels: List<FeatureScopeModel>,
        packageData: PackageData,
    ) = listOf(
        FileSpec.get(
            packageName = packageData.name,
            typeSpec = OrchestraClass().generate(
                featureScopeModels = featureScopeModels,
                packageData = packageData,
            )
        )
    )
}