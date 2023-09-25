package ru.unit.orchestra_features.example.jvm

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.unit.orchestra_features.common.extension.asFlow
import ru.unit.orchestra_features.example.jvm.orchestra_features.generated.TestFirstFeatureScope

fun main(): Unit = runBlocking {
    launch(Dispatchers.IO) {
        TestFirstFeatureScope.awdFeature.asFlow().collect {
            println("-> $it")
        }
    }

    delay(100)

    println("Emit")

    TestFirstFeatureScope.awdFeature.toggle(true)
    TestFirstFeatureScope.awdFeature.toggle(false)
    TestFirstFeatureScope.awdFeature.toggle(false)
    TestFirstFeatureScope.awdFeature.toggle(false)
    TestFirstFeatureScope.awdFeature.toggle(true)
    TestFirstFeatureScope.awdFeature.toggle(false)
    TestFirstFeatureScope.awdFeature.toggle(false)
    TestFirstFeatureScope.awdFeature.toggle(false)
}
