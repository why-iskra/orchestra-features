package ru.unit.orchestra_features.example.jvm

import ru.unit.orchestra_features.common.annotation.FeatureScope

@FeatureScope(dependsOn = [TestFirst.sc::class])
class TestSecond