package coder.apps.space.library.extension

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

/**
 * Shows a Toast message.
 * @param message The message to display, or a string resource ID.
 * @param duration Toast duration (Toast.LENGTH_SHORT or Toast.LENGTH_LONG).
 */
fun Context.showToast(
    message: Any,
    duration: Int = Toast.LENGTH_SHORT
) {
    val text = when (message) {
        is String -> message
        is Int -> getString(message)
        else -> message.toString()
    }
    Toast.makeText(this, text, duration).show()
}

/**
 * Shows a Snackbar with an optional action.
 * @param message The message to display, or a string resource ID.
 * @param duration Snackbar duration (e.g., Snackbar.LENGTH_SHORT).
 * @param actionText Optional action text (e.g., "Undo").
 * @param actionClick Optional action click handler.
 */
fun View.showSnackbar(
    message: Any,
    duration: Int = Snackbar.LENGTH_SHORT,
    actionText: String? = null,
    actionClick: (() -> Unit)? = null
) {
    val text = when (message) {
        is String -> message
        is Int -> context.getString(message)
        else -> message.toString()
    }
    Snackbar.make(this, text, duration).apply {
        actionText?.let { text ->
            setAction(text) { actionClick?.invoke() }
        }
    }.show()
}