package coder.apps.space.library.extension

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import coder.apps.space.library.R

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