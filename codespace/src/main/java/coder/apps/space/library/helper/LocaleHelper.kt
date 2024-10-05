package coder.apps.space.library.helper

import android.content.*
import android.content.res.*
import android.os.*
import java.util.*

var Context.currentLanguage: String?
    get() {
        return TinyDB(this).getString("language", "en") ?: "en"
    }
    set(value) {
        TinyDB(this).putString("language", value)
    }

fun Context.applyLanguageChanges() {
    val language = currentLanguage
    val locale = Locale(language)
    Locale.setDefault(locale)
    val resources = resources
    val configuration = Configuration(resources.configuration)
    configuration.setLocales(LocaleList(locale))
    resources.updateConfiguration(configuration, resources.displayMetrics)
}
