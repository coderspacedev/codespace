package coder.apps.space.library.extension


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

/**
 * Tints a drawable with a color.
 * @param color Color integer.
 * @return Tinted drawable.
 */
fun Drawable.tint(@ColorInt color: Int): Drawable {
    val wrapped = DrawableCompat.wrap(mutate())
    DrawableCompat.setTint(wrapped, color)
    return wrapped
}

/**
 * Tints a drawable with a color resource.
 * @param context Context to access resources.
 * @param colorRes Color resource ID.
 * @return Tinted drawable.
 */
fun Drawable.tint(context: Context, @ColorRes colorRes: Int): Drawable {
    return tint(ContextCompat.getColor(context, colorRes))
}

/**
 * Converts a drawable to a bitmap.
 * @param width Desired width (defaults to intrinsic width).
 * @param height Desired height (defaults to intrinsic height).
 * @return Bitmap representation.
 */
fun Drawable.toBitmap(width: Int = intrinsicWidth, height: Int = intrinsicHeight): Bitmap {
    if (this is BitmapDrawable && bitmap != null) return bitmap
    val w = if (width <= 0) 1 else width
    val h = if (height <= 0) 1 else height
    val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    setBounds(0, 0, w, h)
    draw(canvas)
    return bitmap
}

/**
 * Resizes a drawable.
 * @param context Context to access resources.
 * @param width New width in pixels.
 * @param height New height in pixels.
 * @return Resized drawable.
 */
fun Drawable.resize(context: Context, width: Int, height: Int): Drawable {
    val bitmap = toBitmap(width, height)
    return BitmapDrawable(context.resources, bitmap)
}

/**
 * Adjusts drawable alpha (0 to 255).
 * @param alpha Alpha value (0 = transparent, 255 = opaque).
 * @return Drawable with adjusted alpha.
 */
fun Drawable.withAlpha(alpha: Int): Drawable {
    val drawable = mutate()
    drawable.alpha = alpha.coerceIn(0, 255)
    return drawable
}

/**
 * Applies a color filter to a drawable.
 * @param color Color integer.
 * @param mode PorterDuff mode (defaults to SRC_IN).
 * @return Filtered drawable.
 */
fun Drawable.applyColorFilter(
    @ColorInt color: Int,
    mode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN
): Drawable {
    val drawable = mutate()
    drawable.colorFilter = PorterDuffColorFilter(color, mode)
    return drawable
}

/**
 * Checks if a drawable is null or effectively empty (invalid dimensions).
 * @return True if null or empty.
 */
fun Drawable?.isNullOrEmpty(): Boolean {
    if (this == null) return true
    return when (this) {
        is VectorDrawable, is VectorDrawableCompat -> false // Vectors are valid
        else -> intrinsicWidth <= 1 && intrinsicHeight <= 1
    }
}

/**
 * Rotates a drawable.
 * @param context Context to access resources.
 * @param degrees Rotation angle in degrees.
 * @return Rotated drawable.
 */
fun Drawable.rotate(context: Context, degrees: Float): Drawable {
    val bitmap = toBitmap()
    val matrix = Matrix().apply { postRotate(degrees) }
    val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    return BitmapDrawable(context.resources, rotatedBitmap)
}

/**
 * Scales a drawable proportionally or non-proportionally.
 * @param context Context to access resources.
 * @param scaleX Horizontal scale factor (1.0f = original).
 * @param scaleY Vertical scale factor (1.0f = original).
 * @param keepAspectRatio If true, uses scaleX for both dimensions.
 * @return Scaled drawable.
 */
fun Drawable.scale(
    context: Context,
    scaleX: Float,
    scaleY: Float = scaleX,
    keepAspectRatio: Boolean = true
): Drawable {
    val width = (intrinsicWidth * if (keepAspectRatio) scaleX else scaleX).toInt().coerceAtLeast(1)
    val height =
        (intrinsicHeight * if (keepAspectRatio) scaleX else scaleY).toInt().coerceAtLeast(1)
    return resize(context, width, height)
}

/**
 * Mirrors a drawable horizontally or vertically.
 * @param context Context to access resources.
 * @param horizontal If true, mirrors horizontally.
 * @param vertical If true, mirrors vertically.
 * @return Mirrored drawable.
 */
fun Drawable.mirror(
    context: Context,
    horizontal: Boolean = true,
    vertical: Boolean = false
): Drawable {
    val bitmap = toBitmap()
    val matrix = Matrix().apply {
        if (horizontal) postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        if (vertical) postScale(1f, -1f, bitmap.width / 2f, bitmap.height / 2f)
    }
    val mirroredBitmap =
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    return BitmapDrawable(context.resources, mirroredBitmap)
}

/**
 * Applies a grayscale filter to a drawable.
 * @return Grayscale drawable.
 */
fun Drawable.toGrayscale(): Drawable {
    val drawable = mutate()
    val colorMatrix = ColorMatrix().apply { setSaturation(0f) }
    drawable.colorFilter = ColorMatrixColorFilter(colorMatrix)
    return drawable
}

/**
 * Adds a shadow to a drawable.
 * @param context Context to access resources.
 * @param radius Shadow radius.
 * @param dx Shadow X offset.
 * @param dy Shadow Y offset.
 * @param shadowColor Shadow color.
 * @return Drawable with shadow.
 */
fun Drawable.addShadow(
    context: Context,
    radius: Float,
    dx: Float,
    dy: Float,
    @ColorInt shadowColor: Int
): Drawable {
    val bitmap = toBitmap()
    val shadowBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(shadowBitmap)
    val paint = Paint().apply {
        setShadowLayer(radius, dx, dy, shadowColor)
    }
    canvas.drawBitmap(bitmap, 0f, 0f, paint)
    return BitmapDrawable(context.resources, shadowBitmap)
}

/**
 * Checks if a drawable is a VectorDrawable or VectorDrawableCompat.
 * @return True if vector drawable.
 */
fun Drawable.isVector(): Boolean {
    return this is VectorDrawable || this is VectorDrawableCompat
}