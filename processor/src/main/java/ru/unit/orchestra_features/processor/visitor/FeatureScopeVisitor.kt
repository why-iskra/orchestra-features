package ru.unit.orchestra_features.processor.visitor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.visitor.KSEmptyVisitor
import com.squareup.kotlinpoet.ksp.toClassName
import ru.unit.orchestra_features.common.annotation.Feature
import ru.unit.orchestra_features.common.annotation.FeatureScope
import ru.unit.orchestra_features.processor.exception.ProcessorException
import ru.unit.orchestra_features.processor.model.FeatureScopeModel
import ru.unit.orchestra_features.processor.utils.extension.checkName
import ru.unit.orchestra_features.processor.utils.extension.getAnnotation
import ru.unit.orchestra_features.processor.utils.extension.getParameter

class FeatureScopeVisitor : KSEmptyVisitor<Unit, FeatureScopeModel>() {

    override fun defaultHandler(node: KSNode, data: Unit) = throw ProcessorException(
        message = "Wrong feature scope visitor node ${node.origin.name}",
        node = node
    )

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) = createModel(
        node = classDeclaration,
        defaultName = classDeclaration.simpleName.asString(),
        clazz = classDeclaration.toClassName().canonicalName
    )

    @OptIn(KspExperimental::class)
    private fun <T> createModel(
        node: T,
        defaultName: String,
        clazz: String
    ): FeatureScopeModel where T : KSAnnotated, T : KSDeclarationContainer {
        val featureScopeAnnotation =
            node.getAnnotationsByType(FeatureScope::class).lastOrNull() ?: throw ProcessorException(
                message = "Cannot found FeatureScope annotation",
                node = node
            )

        val dependsOn = node
            .getAnnotation<FeatureScope>()
            .getParameter<List<KSType>>(
                name = "dependsOn",
                default = emptyList()
            ).map { type ->
                type.toClassName().canonicalName
            }

        val name = featureScopeAnnotation.name.let { name ->
            val result = name.ifBlank {
                defaultName.removeFileExtension()
            }

            result.checkName(node)
        }

        val features = node.declarations.mapNotNull { declaration ->
            declaration.getAnnotation<Feature>() ?: return@mapNotNull null

            declaration.accept(
                visitor = FeatureVisitor(),
                data = Unit
            )
        }.toList()

        if ((node is KSDeclaration) && !node.isPublic()) {
            throw ProcessorException(
                message = "Class of feature scope $name isn't public",
                node = node
            )
        }

        return FeatureScopeModel(
            rawName = name,
            dependsOnClasses = dependsOn,
            features = features,
            beautifyName = featureScopeAnnotation.beautifyName,
            clazz = clazz,
            ksNode = node,
            originalKSFile = node.accept(FileVisitor(), Unit)!!
        )
    }

    private fun String.removeFileExtension(): String {
        val lastIndexOfDot = lastIndexOf(".")

        if (lastIndexOfDot < 0) {
            return this
        }

        return substring(0, lastIndexOfDot)
    }
}