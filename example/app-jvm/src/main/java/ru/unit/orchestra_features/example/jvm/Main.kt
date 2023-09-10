package ru.unit.orchestra_features.example.jvm

import `app-jvm`.orchestra_features.generated.Orchestra
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.unit.orchestra_features.common.extension.asFlow

fun main(): Unit = runBlocking {
    launch(Dispatchers.IO) {
        Orchestra.Scope1.awd.asFlow().collect {
            println("-> $it")
        }
    }

    delay(100)

    println("Emit")

    Orchestra.Scope1.awd.toggle(true)
    Orchestra.Scope1.awd.toggle(false)
    Orchestra.Scope1.awd.toggle(false)
    Orchestra.Scope1.awd.toggle(false)
    Orchestra.Scope1.awd.toggle(true)
    Orchestra.Scope1.awd.toggle(false)
    Orchestra.Scope1.awd.toggle(false)
    Orchestra.Scope1.awd.toggle(false)
}
