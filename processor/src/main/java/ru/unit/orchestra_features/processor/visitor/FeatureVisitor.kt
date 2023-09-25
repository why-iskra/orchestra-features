package ru.unit.orchestra_features.processor.visitor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.visitor.KSEmptyVisitor
import com.squareup.kotlinpoet.ksp.toClassName
import ru.unit.orchestra_features.common.annotation.DependsOn
import ru.unit.orchestra_features.common.annotation.Feature
import ru.unit.orchestra_features.common.annotation.Toggleable
import ru.unit.orchestra_features.processor.exception.ProcessorException
import ru.unit.orchestra_features.processor.model.FeatureModel
import ru.unit.orchestra_features.processor.utils.extension.checkName
import ru.unit.orchestra_features.processor.utils.extension.getAnnotations
import ru.unit.orchestra_features.processor.utils.extension.getParameter

class FeatureVisitor : KSEmptyVisitor<Unit, FeatureModel>() {

    override fun defaultHandler(node: KSNode, data: Unit) = throw ProcessorException(
        message = "Wrong feature visitor node",
        node = node
    )

    @OptIn(KspExperimental::class)
    override fun visitClassDeclaration(
        classDeclaration: KSClassDeclaration,
        data: Unit
    ): FeatureModel {
        val featureAnnotation = classDeclaration
            .getAnnotationsByType(Feature::class)
            .lastOrNull() ?: throw ProcessorException(
            message = "Cannot found Feature annotation",
            node = classDeclaration
        )

        val name = featureAnnotation.name.let { name ->
            val result = name.ifBlank {
                classDeclaration.simpleName.asString()
            }

            result.checkName(classDeclaration)
        }

        val dependsOnClasses = classDeclaration.getAnnotations<DependsOn>().flatMap { annotation ->
            annotation
                .getParameter<List<KSType>>(
                    name = "features",
                    default = emptyList()
                ).map { type ->
                    type.toClassName().canonicalName
                }
        }.toList()

        val toggleableAnnotation = classDeclaration
            .getAnnotationsByType(Toggleable::class)
            .lastOrNull() ?: Toggleable()

        if (!classDeclaration.isPublic()) {
            throw ProcessorException(
                message = "Class of feature $name isn't public",
                node = classDeclaration
            )
        }

        return FeatureModel(
            rawName = name,
            description = featureAnnotation.description,
            mutable = featureAnnotation.mutable,
            interactive = featureAnnotation.interactive,
            dependsOnClasses = dependsOnClasses,
            toggleable = toggleableAnnotation,
            beautifyName = featureAnnotation.beautifyName,
            clazz = classDeclaration.toClassName().canonicalName,
            ksNode = classDeclaration,
            originalKSFile = classDeclaration.accept(FileVisitor(), Unit)!!
        )
    }
}