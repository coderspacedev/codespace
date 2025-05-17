package coder.apps.space.library.helper

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import java.util.Locale

var Context.currentLanguage: String?
    get() = TinyDB(this).getString(languagePrefKey, null) ?: Locale.getDefault().language
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
            val localeList = LocaleList(localeToSwitchTo)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                context = context.createConfigurationContext(configuration)
            } else {
                resources.updateConfiguration(configuration, resources.displayMetrics)
            }
            return ContextUtils(context)
        }
    }
}