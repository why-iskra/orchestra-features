package ru.unit.orchestra_features.common.support.interactive

import ru.unit.orchestra_features.common.support.Feature

data class InteractiveFeatureData(
    val element: ElementData,
    val description: String,
    val dependsOn: List<ElementData>,
    val feature: Feature<*>,
    val toggleable: Feature.InteractiveToggleable?,
)
