package coder.apps.space.library.extension

import android.os.Handler
import android.os.Looper

fun Handler.postDelayed(delayMillis: Long, action: () -> Unit) {
    this.postDelayed(Runnable(action), delayMillis)
}

fun Runnable(action: () -> Unit): Runnable {
    return object : Runnable {
        override fun run() {
            action()
        }
    }
}

fun delayed(delayMillis: Long, action: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(Runnable(action), delayMillis)
}