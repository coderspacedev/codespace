package coder.apps.space.library.extension

import android.util.*
import coder.apps.space.library.BuildConfig

fun String.log(message: String) {
    if (BuildConfig.DEBUG) Log.e(this, message)
}