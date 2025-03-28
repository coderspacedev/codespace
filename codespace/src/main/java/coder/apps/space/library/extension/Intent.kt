package coder.apps.space.library.extension

import android.app.*
import android.content.*
import android.os.*


import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

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

inline fun <reified T> Intent.getExtra(key: String, defaultValue: T): T {
    return when (T::class) {
        String::class -> getStringExtra(key) as? T ?: defaultValue
        Int::class -> getIntExtra(key, defaultValue as? Int ?: 0) as T
        Boolean::class -> getBooleanExtra(key, defaultValue as? Boolean ?: false) as T
        Float::class -> getFloatExtra(key, defaultValue as? Float ?: 0f) as T
        Double::class -> getDoubleExtra(key, defaultValue as? Double ?: 0.0) as T
        Long::class -> getLongExtra(key, defaultValue as? Long ?: 0L) as T
        Parcelable::class -> getParcelableExtra<Parcelable>(key) as? T ?: defaultValue
        Serializable::class -> getSerializableExtra(key) as? T ?: defaultValue
        else -> defaultValue
    }
}