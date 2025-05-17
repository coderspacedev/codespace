package coder.apps.space.library.extension

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.content.res.XmlResourceParser
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.MenuInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.annotation.ArrayRes
import androidx.annotation.BoolRes
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.annotation.IntegerRes
import androidx.annotation.MenuRes
import androidx.annotation.PluralsRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.annotation.XmlRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import java.io.InputStream

/**
 * Retrieves a color resource.
 * @param id Color resource ID.
 * @return Color integer.
 */
fun Context.color(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

/**
 * Retrieves a drawable resource.
 * @param id Drawable resource ID.
 * @return Drawable or null if not found.
 */
fun Context.drawable(@DrawableRes id: Int): Drawable? = ContextCompat.getDrawable(this, id)

/**
 * Retrieves a dimension resource.
 * @param id Dimension resource ID.
 * @return Dimension value in pixels.
 */
fun Context.dimen(@DimenRes id: Int): Float = resources.getDimension(id)

/**
 * Retrieves a string resource.
 * @param id String resource ID.
 * @param args Optional format arguments.
 * @return Formatted string or null if not found.
 */
fun Context.string(@StringRes id: Int, vararg args: Any): String? =
    try {
        if (args.isEmpty()) getString(id) else getString(id, *args)
    } catch (e: Exception) {
        null
    }

/**
 * Retrieves a plural string resource.
 * @param id Plural resource ID.
 * @param quantity Quantity to select the plural form.
 * @param args Optional format arguments.
 * @return Formatted plural string or null if not found.
 */
fun Context.plural(@PluralsRes id: Int, quantity: Int, vararg args: Any): String? =
    try {
        resources.getQuantityString(id, quantity, *args)
    } catch (e: Exception) {
        null
    }

/**
 * Retrieves a boolean resource.
 * @param id Boolean resource ID.
 * @return Boolean value or false if not found.
 */
fun Context.bool(@BoolRes id: Int): Boolean = try {
    resources.getBoolean(id)
} catch (e: Exception) {
    false
}

/**
 * Retrieves an integer resource.
 * @param id Integer resource ID.
 * @return Integer value or 0 if not found.
 */
fun Context.integer(@IntegerRes id: Int): Int = try {
    resources.getInteger(id)
} catch (e: Exception) {
    0
}

/**
 * Retrieves a string array resource.
 * @param id Array resource ID.
 * @return List of strings or empty list if not found.
 */
fun Context.stringArray(@ArrayRes id: Int): List<String> = try {
    resources.getStringArray(id).toList()
} catch (e: Exception) {
    emptyList()
}

/**
 * Retrieves an integer array resource.
 * @param id Array resource ID.
 * @return List of integers or empty list if not found.
 */
fun Context.intArray(@ArrayRes id: Int): List<Int> = try {
    resources.getIntArray(id).toList()
} catch (e: Exception) {
    emptyList()
}

/**
 * Retrieves a typed array resource.
 * @param id Array resource ID.
 * @return TypedArray or null if not found.
 */
fun Context.typedArray(@ArrayRes id: Int): TypedArray? = try {
    resources.obtainTypedArray(id)
} catch (e: Exception) {
    null
}

/**
 * Retrieves a color state list resource.
 * @param id Color state list resource ID.
 * @return ColorStateList or null if not found.
 */
fun Context.colorStateList(@ColorRes id: Int): ColorStateList? =
    ContextCompat.getColorStateList(this, id)

/**
 * Retrieves an animation resource.
 * @param id Animation resource ID.
 * @return Animation or null if not found.
 */
fun Context.animation(@AnimRes id: Int): Animation? = try {
    AnimationUtils.loadAnimation(this, id)
} catch (e: Exception) {
    null
}

/**
 * Retrieves a raw resource as an InputStream.
 * @param id Raw resource ID.
 * @return InputStream or null if not found.
 */
fun Context.rawResource(@RawRes id: Int): InputStream? = try {
    resources.openRawResource(id)
} catch (e: Exception) {
    null
}

/**
 * Retrieves a font resource.
 * @param id Font resource ID.
 * @return Typeface or null if not found.
 */
fun Context.font(@FontRes id: Int): Typeface? = try {
    ResourcesCompat.getFont(this, id)
} catch (e: Exception) {
    null
}

/**
 * Retrieves an XML resource parser.
 * @param id XML resource ID.
 * @return XmlResourceParser or null if not found.
 */
fun Context.xmlResource(@XmlRes id: Int): XmlResourceParser? = try {
    resources.getXml(id)
} catch (e: Exception) {
    null
}

/**
 * Retrieves a menu resource.
 * @param id Menu resource ID.
 * @param menu Menu to inflate into.
 * @return True if inflation succeeds, false otherwise.
 */
fun Context.menu(@MenuRes id: Int, menu: android.view.Menu): Boolean = try {
    MenuInflater(this).inflate(id, menu)
    true
} catch (e: Exception) {
    false
}