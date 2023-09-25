package ru.unit.orchestra_features.processor.generator.code.scope

import com.squareup.kotlinpoet.FileSpec
import ru.unit.orchestra_features.processor.model.FeatureScopeModel
import ru.unit.orchestra_features.processor.utils.PackageData

class ScopeFile {

    companion object {

        fun pack(
            model: FeatureScopeModel,
            packageData: PackageData
        ) = model.originalKSFile.packageName.asString() + ".${packageData.suffix}"
    }

    fun generate(
        featureScopeModel: FeatureScopeModel,
        packageData: PackageData
    ) = FileSpec.get(
        packageName = pack(
            model = featureScopeModel,
            packageData = packageData
        ),
        typeSpec = ScopeClass().generate(
            featureScopeModel = featureScopeModel,
            packageData = packageData
        )
    )
}