package coder.apps.space.library.extension

import android.content.Intent
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import coder.apps.space.library.R

fun TextView.highlightText(text: String, color: Int) {
    val fullText = this.text.toString()
    val spannableBuilder = SpannableStringBuilder(fullText)
    var startIndex = fullText.indexOf(text)
    while (startIndex != -1) {
        val endIndex = startIndex + text.length
        val span = ForegroundColorSpan(color)
        spannableBuilder.setSpan(span, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        startIndex = fullText.indexOf(text, endIndex)
    }

    this.text = spannableBuilder
}

data class ClickableAction(
    val text: String,
    val url: String? = null,
    val onClick: (() -> Unit)? = null
)

fun TextView.setClickableText(totalText: String, clickableItems: List<ClickableAction>) {
    val spannable = SpannableString(totalText)
    clickableItems.forEach { item ->
        val startIndex = totalText.indexOf(item.text)
        if (startIndex >= 0) {
            val endIndex = startIndex + item.text.length
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: android.view.View) {
                    item.url?.let {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(it)
                            )
                        )
                    }
                        ?: item.onClick?.invoke()
                }
            }
            spannable.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
    text = spannable
    movementMethod = LinkMovementMethod.getInstance()
}