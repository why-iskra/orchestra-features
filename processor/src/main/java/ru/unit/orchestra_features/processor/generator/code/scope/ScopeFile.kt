package ru.unit.orchestra_features.processor.generator.code.scope

import com.squareup.kotlinpoet.FileSpec
import ru.unit.orchestra_features.processor.model.FeatureScopeModel

class ScopeFile {

    companion object {

        fun pack(
            model: FeatureScopeModel,
            packageName: String
        ) = model.originalKSFile.packageName.asString() + ".$packageName"
    }

    fun generate(
        featureScopeModel: FeatureScopeModel,
        packageName: String
    ) = FileSpec.get(
        packageName = pack(
            model = featureScopeModel,
            packageName = packageName
        ),
        typeSpec = ScopeClass().generate(
            featureScopeModel = featureScopeModel,
            packageName = packageName
        )
    )
}