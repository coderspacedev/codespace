package coder.apps.space.library.helper

import android.content.*
import android.content.res.*
import android.os.*
import java.util.*

var Context.currentLanguage: String?
    get() = TinyDB(this).getString(languagePrefKey, "en") ?: "en"
    set(value) = TinyDB(this).putString(languagePrefKey, value)

var Context.languagePrefKey: String?
    get() = TinyDB(this).getString("languagePrefKey", "language") ?: "language"
    set(value) = TinyDB(this).putString("languagePrefKey", value)

class ContextUtils(base: Context) : ContextWrapper(base) {

    companion object {

        fun updateLocale(c: Context, localeToSwitchTo: Locale): ContextWrapper {
            var context = c
            val resources: Resources = context.resources
            val configuration: Configuration = resources.configuration
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val localeList = LocaleList(localeToSwitchTo)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)
            } else {
                configuration.locale = localeToSwitchTo
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                context = context.createConfigurationContext(configuration)
            } else {
                resources.updateConfiguration(configuration, resources.displayMetrics)
            }
            return ContextUtils(context)
        }
    }
}