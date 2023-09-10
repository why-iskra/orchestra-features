package ru.unit.orchestra_features.processor.generator.code.orchestra.property

import com.squareup.kotlinpoet.PropertySpec
import ru.unit.orchestra_features.processor.generator.code.feature.FeatureClass
import ru.unit.orchestra_features.processor.model.FeatureModel
import ru.unit.orchestra_features.processor.utils.extension.asClassName

class FeatureScopeProperty {

    companion object {

        fun name(featureModel: FeatureModel) = featureModel.name
    }

    fun generate(
        featureModel: FeatureModel,
        packageName: String
    ) = PropertySpec.builder(
        name = name(featureModel),
        type = "${packageName}.${FeatureClass.name(featureModel)}".asClassName()
    ).apply {
        val className = FeatureClass.name(featureModel).asClassName()

//        if (featureModel.isInteractive) {
//            addAnnotation(
//                AnnotationSpec.builder(InteractiveFeature::class).apply {
//                    val name = buildString {
//                        append("[")
//                        append(featureModel.name.encodeToByteArray().joinToString(","))
//                        append("]")
//                    }
//
//                    val description = buildString {
//                        append("[")
//                        append((featureModel.description ?: "").encodeToByteArray().joinToString(","))
//                        append("]")
//                    }
//
//                    val isMutable = featureModel.toggleable.interactive.isEnabled
//
//                    addMember(name)
//                    addMember(description)
//                    addMember(isMutable.toString())
//                }.build()
//            )
//        }

        featureModel.description?.let(::addKdoc)

        delegate("lazy { %T() }", className)
    }.build()
}