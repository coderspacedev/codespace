package coder.apps.space.library.extension

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Get file size in human-readable format
fun File.sizeToString(): String {
    val bytes = length()
    return when {
        bytes >= 1_073_741_824 -> "%.2f GB".format(bytes / 1_073_741_824.0)
        bytes >= 1_048_576 -> "%.2f MB".format(bytes / 1_048_576.0)
        bytes >= 1_024 -> "%.2f KB".format(bytes / 1_024.0)
        else -> "$bytes bytes"
    }
}

// Get last modified date as formatted string
fun File.lastModifiedDate(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    val date = Date(lastModified())
    return SimpleDateFormat(pattern, Locale.getDefault()).format(date)
}

// Rename file
fun File.renameTo(newName: String): Boolean {
    val newFile = File(parent, newName)
    return try {
        renameTo(newFile)
    } catch (e: SecurityException) {
        e.printStackTrace()
        false
    }
}

// Check if file is empty
fun File.isEmpty(): Boolean {
    return exists() && length() == 0L
}