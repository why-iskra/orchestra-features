package ru.unit.orchestra_features.common.support.interactive

data class InteractiveScopeData(
    val element: ElementData,
    val dependsOn: List<ElementData>,
    val features: List<InteractiveFeatureData>,
)
