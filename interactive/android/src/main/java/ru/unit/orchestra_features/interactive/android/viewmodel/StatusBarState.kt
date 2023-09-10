package ru.unit.orchestra_features.interactive.android.viewmodel

sealed interface StatusBarState {

    object Light : StatusBarState

    object Dark : StatusBarState
}