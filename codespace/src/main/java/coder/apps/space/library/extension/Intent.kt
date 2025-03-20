package coder.apps.space.library.extension

import android.app.*
import android.content.*
import android.os.*

fun Activity.go(destination: Class<*>, extras: List<Pair<String, Any?>>? = null, finish: Boolean = false, finishAll: Boolean = false): Intent {
    return Intent(this, destination).apply {
        flags = if (finishAll) Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        else Intent.FLAG_ACTIVITY_CLEAR_TOP
        extras?.forEach { (key, value) ->
            when (value) {
                is Int -> putExtra(key, value)
                is Long -> putExtra(key, value)
                is String -> putExtra(key, value)
                is Boolean -> putExtra(key, value)
                is Parcelable -> putExtra(key, value)
                is ArrayList<*> -> putExtra(key, value)
            }
        }
        startActivity(this)
        if (finish) finish()
    }
}

fun Activity.goResult(destination: Class<*>, extras: List<Pair<String, Any?>>? = null, finish: Boolean = false, finishAll: Boolean = false): Intent {
    return Intent(this, destination).apply {
        flags = if (finishAll) Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        else Intent.FLAG_ACTIVITY_CLEAR_TOP
        extras?.forEach { (key, value) ->
            when (value) {
                is Int -> putExtra(key, value)
                is String -> putExtra(key, value)
                is Long -> putExtra(key, value)
                is Boolean -> putExtra(key, value)
                is Parcelable -> putExtra(key, value)
                is ArrayList<*> -> putExtra(key, value)
            }
        }
    }
}