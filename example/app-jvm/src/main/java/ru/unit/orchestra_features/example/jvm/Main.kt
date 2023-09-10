package ru.unit.orchestra_features.example.jvm

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.unit.orchestra_features.common.extension.asFlow
import ru.unit.orchestra_features.example.jvm.orchestra_features.generated.Scope1

fun main(): Unit = runBlocking {
    launch(Dispatchers.IO) {
        Scope1.features.awd.asFlow().collect {
            println("-> $it")
        }
    }

    delay(100)

    println("Emit")

    Scope1.features.awd.toggle(true)
    Scope1.features.awd.toggle(false)
    Scope1.features.awd.toggle(false)
    Scope1.features.awd.toggle(false)
    Scope1.features.awd.toggle(true)
    Scope1.features.awd.toggle(false)
    Scope1.features.awd.toggle(false)
    Scope1.features.awd.toggle(false)
}
