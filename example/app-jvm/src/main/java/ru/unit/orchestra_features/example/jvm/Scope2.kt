package ru.unit.orchestra_features.example.jvm

import ru.unit.orchestra_features.common.annotation.FeatureScope

@FeatureScope(dependsOn = [Scope1.sc::class])
class Scope2