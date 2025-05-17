package coder.apps.space.library.extension

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

fun Context.shareText(text: String, subject: String = "") {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, text)
    }
    startActivity(Intent.createChooser(intent, "Share via"))
}

fun Context.shareTextToApp(text: String, targetPackage: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
        `package` = targetPackage
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show()
    }
}

fun Context.shareFile(file: File, mimeType: String = "*/*") {
    val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = mimeType
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(Intent.createChooser(intent, "Share File"))
}

fun Context.shareFiles(files: MutableList<File>, mimeType: String = "*/*") {
    val uris = files.map {
        FileProvider.getUriForFile(this, "$packageName.provider", it)
    }

    val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
        type = mimeType
        putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(uris))
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    startActivity(Intent.createChooser(intent, "Share Files"))
}

fun Context.shareFilesToApp(files: List<File>, targetPackage: String, mimeType: String = "*/*") {
    val uris = files.map {
        FileProvider.getUriForFile(this, "$packageName.fileprovider", it)
    }

    val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
        type = mimeType
        putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(uris))
        `package` = targetPackage
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        Toast.makeText(this, "Target app not installed", Toast.LENGTH_SHORT).show()
    }
}


