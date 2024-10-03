package coder.apps.space.library.extension

import android.*
import android.app.*
import android.content.*
import android.util.*

val Activity.actionBarHeight: Int
    get() {
        val tv = TypedValue()
        return if (theme.resolveAttribute(R.attr.actionBarSize, tv, true)) {
            TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        } else 0
    }

val Context.statusBarHeight: Int
    get() {
        val resources = resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

val Context.hasNavBar: Boolean
    get() {
        return try {
            val systemPropertiesClass = Class.forName("android.os.SystemProperties")
            val getMethod = systemPropertiesClass.getMethod("get", String::class.java)
            val navBarOverride = getMethod.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
            navBarOverride != "1"
        } catch (e: Exception) {
            val resourceId = resources.getIdentifier("config_showNavigationBar", "bool", "android")
            resourceId > 0 && resources.getBoolean(resourceId)
        }
    }

val Context.navigationBarHeight: Int
    get() {
        return if (hasNavBar) {
            val resources = resources
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                resources.getDimensionPixelSize(resourceId)
            } else 0
        } else 0
    }
