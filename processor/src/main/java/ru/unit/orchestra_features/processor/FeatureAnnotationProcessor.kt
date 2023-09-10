package ru.unit.orchestra_features.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.ksp.writeTo
import ru.unit.orchestra_features.common.annotation.FeatureScope
import ru.unit.orchestra_features.processor.exception.ProcessorException
import ru.unit.orchestra_features.processor.generator.Code
import ru.unit.orchestra_features.processor.visitor.FeatureScopeVisitor
import kotlin.reflect.KClass

class FeatureAnnotationProcessor(
    private val environment: SymbolProcessorEnvironment,
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        runCatching {
            internalProcess(resolver)
        }.onFailure { exception ->
            if (exception is ProcessorException) {
                environment.logger.error(
                    message = exception.message,
                    symbol = exception.node
                )

                exception.additionalMessages.forEach { message ->
                    environment.logger.error(message)
                }
            } else {
                throw exception
            }
        }

        return emptyList()
    }

    private fun internalProcess(resolver: Resolver) {
        val featureScopeAnnotations = resolver.findAnnotations(FeatureScope::class).toList()

        if (featureScopeAnnotations.isEmpty()) {
            return
        }

        val featureScopeModels = featureScopeAnnotations.map { node ->
            node.accept(FeatureScopeVisitor(), Unit)
        }

        val files = Code(
            environment = environment,
            packageName = PACKAGE
        ).generateFiles(featureScopeModels)

        val originalFiles = featureScopeModels.flatMap { featureScopeModel ->
            listOf(
                featureScopeModel.originalKSFile
            ) + featureScopeModel.features.map { model ->
                model.originalKSFile
            }
        }

        files.forEach { file ->
            file.writeTo(
                codeGenerator = environment.codeGenerator,
                aggregating = true,
                originatingKSFiles = originalFiles
            )
        }
    }

    private fun Resolver.findAnnotations(
        kClass: KClass<*>,
    ) = getSymbolsWithAnnotation(kClass.qualifiedName.toString())

    companion object {

        const val PACKAGE = "orchestra_features.generated"
    }
}