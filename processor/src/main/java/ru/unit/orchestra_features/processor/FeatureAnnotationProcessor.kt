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

        val featureScopeModels = featureScopeAnnotations.mapNotNull { node ->
            node.accept(FeatureScopeVisitor(), Unit)
        }

        val files = ru.unit.orchestra_features.processor.generator.Code(
            environment = environment,
            packageName = packageName(resolver)
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

    private fun packageName(resolver: Resolver) = "${getModuleName(resolver)}$PACKAGE_SUFFIX"

    private fun getModuleName(resolver: Resolver): String {
        val moduleDescriptor = resolver::class.java
            .getDeclaredField("module")
            .apply { isAccessible = true }
            .get(resolver)

        val rawName = moduleDescriptor::class.java
            .getMethod("getName")
            .invoke(moduleDescriptor)
            .toString()

        return rawName.removeSurrounding("<", ">")
    }

    companion object {

        const val PACKAGE_SUFFIX = ".orchestra_features.generated"
    }
}