package coder.apps.space.library.extension

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

// Copy text to clipboard
fun Context.copyToClipboard(text: String, label: String = "Copied Text"): Boolean {
    return try {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

// Get text from clipboard
fun Context.getTextFromClipboard(): String? {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    return clipboard.primaryClip?.let { clip ->
        if (clip.itemCount > 0) {
            clip.getItemAt(0).text?.toString()
        } else {
            null
        }
    }
}

// Check if clipboard has text
fun Context.hasTextInClipboard(): Boolean {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    return clipboard.hasPrimaryClip() && clipboard.primaryClip?.itemCount ?: 0 > 0
}

// Clear clipboard
fun Context.clearClipboard(): Boolean {
    return try {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("", ""))
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

// Copy URI (e.g., a link) to clipboard
fun Context.copyUriToClipboard(uri: String, label: String = "Link"): Boolean {
    return try {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newUri(contentResolver, label, android.net.Uri.parse(uri))
        clipboard.setPrimaryClip(clip)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

// Check if clipboard contains a URI
fun Context.hasUriInClipboard(): Boolean {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    return clipboard.hasPrimaryClip() && clipboard.primaryClip?.getItemAt(0)?.uri != null
}

// Get URI from clipboard
fun Context.getUriFromClipboard(): android.net.Uri? {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    return clipboard.primaryClip?.let { clip ->
        if (clip.itemCount > 0) clip.getItemAt(0).uri else null
    }
}