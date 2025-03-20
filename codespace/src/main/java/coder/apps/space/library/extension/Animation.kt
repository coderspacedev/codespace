package coder.apps.space.library.extension

import android.view.View

fun View.zoomIn() {
    animate().alpha(0f).setDuration(500).withEndAction {
        visibility = View.GONE
        alpha = 1f
    }.start()
}

fun View.zoomOut() {
    visibility = View.VISIBLE
    alpha = 0f
    animate().alpha(1f).setDuration(500).start()
}