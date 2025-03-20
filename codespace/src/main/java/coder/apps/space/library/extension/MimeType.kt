package coder.apps.space.library.extension

import android.webkit.*
import java.io.*

val File.mimeType: String
    get() = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension).toString()
val File.isWord: Boolean
    get() = mimeType.startsWith("application/msword") || mimeType.startsWith("application/vnd.ms-word") || mimeType.startsWith(
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    )
val File.isPpt: Boolean
    get() = mimeType.startsWith("application/vnd.ms-powerpoint") || mimeType.startsWith("application/vnd.openxmlformats-officedocument.presentationml")
val File.isExcel: Boolean
    get() = mimeType.startsWith("application/vnd.ms-excel") || mimeType.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml")
val File.isPdf: Boolean
    get() = mimeType.startsWith("application/pdf")
val File.isText: Boolean
    get() = mimeType.startsWith("text")
val File.isImage: Boolean
    get() = mimeType.startsWith("image")
val File.isVideo: Boolean
    get() = mimeType.startsWith("video")
val File.isAudio: Boolean
    get() = mimeType.startsWith("audio")
val File.isZip: Boolean
    get() = extension.equals("zip", ignoreCase = true) || mimeType == "application/zip"
val File.isRar: Boolean
    get() = extension.equals("rar", ignoreCase = true) || mimeType == "application/x-rar-compressed"
val File.isApk: Boolean
    get() = extension.equals("apk", ignoreCase = true)
val File.isCodeFile: Boolean
    get() = extension.equals("java", ignoreCase = true) ||
            extension.equals("kt", ignoreCase = true) ||
            extension.equals("py", ignoreCase = true) ||
            extension.equals("js", ignoreCase = true) ||
            extension.equals("cpp", ignoreCase = true) ||
            extension.equals("c", ignoreCase = true) ||
            extension.equals("html", ignoreCase = true) ||
            extension.equals("css", ignoreCase = true) ||
            extension.equals("php", ignoreCase = true) ||
            extension.equals("rb", ignoreCase = true)
val File.isSvg: Boolean
    get() = extension.equals("svg", ignoreCase = true) || mimeType == "image/svg+xml"
val File.isRecording: Boolean
    get() = extension.equals("wav", ignoreCase = true) ||
            extension.equals("m4a", ignoreCase = true) ||
            extension.equals("aac", ignoreCase = true) ||
            extension.equals("ogg", ignoreCase = true) ||
            extension.equals("flac", ignoreCase = true) ||
            extension.equals("wma", ignoreCase = true)