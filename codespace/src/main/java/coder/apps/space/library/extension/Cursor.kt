package coder.apps.space.library.extension

import android.database.Cursor

fun Cursor.getStringOrNull(columnName: String): String? =
    getColumnIndex(columnName).takeIf { it != -1 }?.let { getString(it) }

fun Cursor.getLongOrNull(columnName: String): Long? =
    getColumnIndex(columnName).takeIf { it != -1 }?.let { getLong(it) }

fun Cursor.getIntOrNull(columnName: String): Int? =
    getColumnIndex(columnName).takeIf { it != -1 }?.let { getInt(it) }

fun Cursor.getBoolean(columnName: String): Boolean =
    getIntOrNull(columnName) == 1

inline fun Cursor.forEach(action: (Cursor) -> Unit) {
    if (moveToFirst()) {
        do {
            action(this)
        } while (moveToNext())
    }
}

inline fun <T> Cursor.map(transform: (Cursor) -> T): List<T> {
    val list = mutableListOf<T>()
    forEach { list.add(transform(it)) }
    return list
}
