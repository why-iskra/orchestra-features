package ru.unit.orchestra_features.processor.provider

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import ru.unit.orchestra_features.processor.FeatureAnnotationProcessor

class FeatureAnnotationProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment) =
        FeatureAnnotationProcessor(environment)
}