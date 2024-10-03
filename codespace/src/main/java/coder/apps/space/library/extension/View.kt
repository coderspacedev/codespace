package coder.apps.space.library.extension

import android.text.*
import android.text.style.*
import android.view.*
import android.widget.*
import androidx.core.content.res.*
import coder.apps.space.library.*

fun View.beInvisibleIf(beInvisible: Boolean) = if (beInvisible) beInvisible() else beVisible()

fun View.beVisibleIf(beVisible: Boolean) = if (beVisible) beVisible() else beGone()

fun View.beGoneIf(beGone: Boolean) = beVisibleIf(!beGone)

fun View.beInvisible() {
    visibility = View.INVISIBLE
}

fun View.beVisible() {
    visibility = View.VISIBLE
}

fun View?.removeSelf() {
    this ?: return
    val parentView = parent as? ViewGroup ?: return
    parentView.removeView(this)
}

fun View.beGone() {
    visibility = View.GONE
}

fun View.beEnableIf(isEnable: Boolean) = if (isEnable) enable() else disable()

fun View.enable() {
    isEnabled = true
    alpha = 1.0F
}

fun View.disable() {
    isEnabled = false
    alpha = 0.5F
}

fun disableComponents(vararg components: View) {
    components.forEach { it.disable() }
}

fun enableComponents(vararg components: View) {
    components.forEach { it.beEnableIf(!it.isEnabled) }
}

fun View.updateViewMargins(leftMargin: Int = 0, topMargin: Int = 0, rightMargin: Int = 0, bottomMargin: Int = 0) {
    val params = layoutParams as LinearLayout.LayoutParams
    params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin)
    layoutParams = params
}

fun View.zoomIn() {
    animate().alpha(0f).setDuration(500).withEndAction {
        visibility = View.GONE
        alpha = 1f
    }.start()
}

fun View.zoomOut() {
    visibility = View.VISIBLE
    alpha = 0f
    animate().alpha(1f).setDuration(500).start()
}

fun TextView.highlightText(text: String, color: Int) {
    val fullText = this.text.toString()
    val spannableBuilder = SpannableStringBuilder(fullText)
    var startIndex = fullText.indexOf(text)
    val typeface = ResourcesCompat.getFont(context, R.font.bold)
    while (startIndex != -1) {
        val endIndex = startIndex + text.length
        val span = ForegroundColorSpan(color)
        spannableBuilder.setSpan(span, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        startIndex = fullText.indexOf(text, endIndex)
    }

    this.text = spannableBuilder
}