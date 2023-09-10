package ru.unit.orchestra_features.interactive.android.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class AppBarBottomSheetBehavior<T : View>(
    context: Context, attrs: AttributeSet
) : CoordinatorLayout.Behavior<T>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: T, dependency: View): Boolean {
        return getBottomSheetBehavior(dependency)?.run {
            peekHeight = parent.height - child.height
            isHideable = false
        } != null
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: T, dependency: View): Boolean {
        val bottom = child.bottom
        val top = dependency.top

        return if (top < bottom) {
            child.translationY = (top - bottom).toFloat()

            true
        } else {
            child.translationY = 0f

            false
        }
    }

    private fun getBottomSheetBehavior(view: View): BottomSheetBehavior<*>? {
        val params = view.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior

        return behavior as? BottomSheetBehavior<*>
    }
}