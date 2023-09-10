package ru.unit.orchestra_features.interactive.android

import ru.unit.orchestra_features.common.support.interactive.InteractiveFeatureScope
import java.util.*

object AndroidFeatureScopeInjector {

    private val scopeList = Collections.synchronizedList(mutableListOf<InteractiveFeatureScope>())
    internal val injected: List<InteractiveFeatureScope> = scopeList

    fun inject(vararg scopes: InteractiveFeatureScope) {
        inject(scopes.toList())
    }

    fun inject(scopes: List<InteractiveFeatureScope>) {
        scopeList.addAll(scopes)
    }

    fun remove(vararg scopes: InteractiveFeatureScope) {
        remove(scopes.toList())
    }

    fun remove(scopes: List<InteractiveFeatureScope>) {
        scopeList.removeAll(scopes)
    }
}