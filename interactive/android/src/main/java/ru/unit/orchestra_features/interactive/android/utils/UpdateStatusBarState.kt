package ru.unit.orchestra_features.interactive.android.utils

import android.app.Activity
import android.os.Build
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.core.content.ContextCompat
import ru.unit.orchestra_features.interactive.android.R
import ru.unit.orchestra_features.interactive.android.viewmodel.StatusBarState

fun Activity?.updateStatusBarState(state: StatusBarState) {
    this ?: return

    val (isLight, color) = when (state) {
        StatusBarState.Dark -> (false to R.color.ofia_black)
        StatusBarState.Light -> (true to R.color.ofia_white)
    }

    val window = window ?: return

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val systemUiAppearance = if (isLight) {
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        } else {
            0
        }

        window.insetsController?.setSystemBarsAppearance(
            /* appearance = */ systemUiAppearance,
            /* mask = */ WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, color)
    }
}