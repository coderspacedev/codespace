package coder.apps.space.library.helper

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class TinyDB(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("${context.packageName}_preferences", Context.MODE_PRIVATE)
    fun getInt(key: String?, value: Int): Int {
        return preferences.getInt(key, value)
    }

    fun getLong(key: String?): Long {
        return preferences.getLong(key, 0)
    }

    fun getFloat(key: String?): Float {
        return preferences.getFloat(key, 0f)
    }

    fun getDouble(key: String?): Double {
        val number = getString(key, "")
        return try {
            number!!.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    fun getString(key: String?, value: String?): String? {
        return preferences.getString(key, value)
    }

    fun getBoolean(key: String?, value: Boolean): Boolean {
        return preferences.getBoolean(key, value)
    }

    fun <T> getObject(key: String?, classOfT: Class<T>?): T? {
        val json = getString(key, "")
        val value: T = Gson().fromJson(json, classOfT) ?: return null
        return value
    }

    fun putInt(key: String?, value: Int) {
        checkForNullKey(key)
        preferences.edit().putInt(key, value).apply()
    }

    fun putLong(key: String?, value: Long) {
        checkForNullKey(key)
        preferences.edit().putLong(key, value).apply()
    }

    fun putFloat(key: String?, value: Float) {
        checkForNullKey(key)
        preferences.edit().putFloat(key, value).apply()
    }

    fun putDouble(key: String?, value: Double) {
        checkForNullKey(key)
        putString(key, value.toString())
    }

    fun putString(key: String?, value: String?) {
        checkForNullKey(key)
        checkForNullValue(value)
        preferences.edit().putString(key, value).apply()
    }

    fun putBoolean(key: String?, value: Boolean) {
        checkForNullKey(key)
        preferences.edit().putBoolean(key, value).apply()
    }

    fun putObject(key: String?, obj: Any?) {
        checkForNullKey(key)
        val gson = Gson()
        putString(key, gson.toJson(obj))
    }

    fun remove(key: String?) {
        preferences.edit().remove(key).apply()
    }

    fun clear() {
        preferences.edit().clear().apply()
    }

    private fun checkForNullKey(key: String?) {
        if (key == null) {
            throw NullPointerException()
        }
    }

    private fun checkForNullValue(value: String?) {
        if (value == null) {
            throw NullPointerException()
        }
    }
}