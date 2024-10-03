package coder.apps.space.library.extension

import android.content.*
import androidx.appcompat.app.*
import coder.apps.space.library.helper.*

fun Context.themeToggleMode() {
    when (TinyDB(this).getInt(THEME, 3)) {
        1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}