package coder.apps.space.library.extension

import android.app.*
import android.content.*
import android.os.*


import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

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

inline fun <reified T> Intent.getExtra(key: String, defaultValue: T? = null): T? {
    val clazz: KClass<*> = T::class
    return when {
        clazz == String::class -> getStringExtra(key) as? T ?: defaultValue
        clazz == Int::class -> getIntExtra(key, defaultValue as? Int ?: 0) as T
        clazz == Boolean::class -> getBooleanExtra(key, defaultValue as? Boolean ?: false) as T
        clazz == Float::class -> getFloatExtra(key, defaultValue as? Float ?: 0f) as T
        clazz == Double::class -> getDoubleExtra(key, defaultValue as? Double ?: 0.0) as T
        clazz == Long::class -> getLongExtra(key, defaultValue as? Long ?: 0L) as T
        clazz == Bundle::class -> getBundleExtra(key) as? T ?: defaultValue
        clazz.isSubclassOf(Parcelable::class) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelableExtra(key, T::class.java)
            } else {
                @Suppress("DEPRECATION")
                getParcelableExtra(key)
            } as? T ?: defaultValue
        }

        clazz.isSubclassOf(Serializable::class) -> getSerializableExtra(key) as? T ?: defaultValue
        else -> defaultValue
    }
}
