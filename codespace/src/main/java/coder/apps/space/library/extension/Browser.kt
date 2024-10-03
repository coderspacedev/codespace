package coder.apps.space.library.extension

import android.app.*
import android.content.*
import android.net.*
import androidx.browser.customtabs.*
import androidx.core.content.*
import coder.apps.space.library.*

fun Activity.launchUrl(url: String?) {
    try {
        val intent = CustomTabsIntent.Builder()
        intent.setToolbarColor(ContextCompat.getColor(this, R.color.colorAccent))
        val tabsIntent = CustomTabsIntent.Builder().build()
        tabsIntent.launchUrl(this, Uri.parse(url))
    } catch (e: Exception) {
        try {
            val newIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            if (newIntent.resolveActivity(packageManager) != null) {
                startActivity(newIntent)
            }
        } catch (exception: ActivityNotFoundException) {
            val newIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            if (newIntent.resolveActivity(packageManager) != null) {
                startActivity(newIntent)
            }
        }
    }
}