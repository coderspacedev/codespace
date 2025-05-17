package coder.apps.space.library.helper

import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import coder.apps.space.library.extension.log

/**
 * Utility class for media operations on Android API 29+ (Scoped Storage).
 * Handles creating, deleting, and renaming media files with permission requests.
 */
class AndroidXI private constructor(private val context: Context) {

    /**
     * Creates a new media file in the specified directory.
     * @param directory Relative path (e.g., "Music/MyFolder") or null for default.
     * @param filename Name of the file (e.g., "song.mp3").
     * @param mimeType MIME type (e.g., "audio/mpeg").
     * @param mediaType Type of media ("audio" or "video").
     * @return Uri of the created file, or null if creation fails.
     */
    fun create(directory: String?, filename: String?, mimeType: String?, mediaType: String): Uri? {
        val contentUri = when (mediaType.lowercase()) {
            "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            else -> throw IllegalArgumentException("Unsupported media type: $mediaType")
        }
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, directory ?: mediaType)
            }
        }
        return context.contentResolver.insert(contentUri, values)
    }

    /**
     * Deletes multiple media URIs, requesting permission if needed.
     * @param launcher ActivityResultLauncher for permission requests.
     * @param uris List of URIs to delete.
     * @param mediaType Type of media ("audio" or "video").
     * @param onSuccess Called per successful deletion.
     * @param onFailed Called per failed deletion.
     * @param onComplete Called when deletion completes.
     */
    fun deleteMultiple(
        launcher: ActivityResultLauncher<IntentSenderRequest>,
        uris: List<Uri>,
        mediaType: String,
        onSuccess: () -> Unit = {},
        onFailed: () -> Unit = {},
        onComplete: () -> Unit = {}
    ) {
        val contentUri = when (mediaType.lowercase()) {
            "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            else -> throw IllegalArgumentException("Unsupported media type: $mediaType")
        }
        val urisToDelete = mutableListOf<Uri>()

        try {
            uris.forEach { uri ->
                val fileId = context.findMediaID(uri.toString(), mediaType)
                val mediaUri =
                    fileId.takeIf { it != 0L }?.let { ContentUris.withAppendedId(contentUri, it) }
                if (mediaUri != null && context.contentResolver.delete(mediaUri, null, null) > 0) {
                    onSuccess()
                } else {
                    onFailed()
                    mediaUri?.let { urisToDelete.add(it) }
                }
            }
            onComplete()
        } catch (e: SecurityException) {
            requestDeletePermission(launcher, urisToDelete, e)
        }
    }

    /**
     * Renames a media file, requesting permission if needed.
     * @param launcher ActivityResultLauncher for permission requests.
     * @param uri URI of the media file.
     * @param newName New filename (e.g., "new_song.mp3").
     * @param onRename Called when renaming succeeds.
     */
    fun renameMedia(
        launcher: ActivityResultLauncher<IntentSenderRequest>,
        uri: Uri?,
        newName: String?,
        onRename: () -> Unit
    ) {
        if (uri == null || newName.isNullOrBlank()) return
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, newName)
        }
        try {
            context.contentResolver.update(uri, values, null, null)
            onRename()
        } catch (e: SecurityException) {
            requestWritePermission(launcher, listOf(uri))
        }
    }

    /**
     * Requests write access for media URIs.
     * @param launcher ActivityResultLauncher for permission requests.
     * @param uris List of URIs to request access for.
     */
    fun requestWriteAccess(
        launcher: ActivityResultLauncher<IntentSenderRequest>,
        uris: List<Uri>
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || uris.isEmpty()) return
        MediaStore.createWriteRequest(context.contentResolver, uris).let {
            launcher.launch(IntentSenderRequest.Builder(it.intentSender).build())
        }
    }

    /**
     * Requests write permission for a list of URIs.
     * @param launcher ActivityResultLauncher for permission requests.
     * @param uris List of URIs to request write access for.
     */
    fun requestWritePermission(
        launcher: ActivityResultLauncher<IntentSenderRequest>,
        uris: List<Uri>
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || uris.isEmpty()) return
        MediaStore.createWriteRequest(context.contentResolver, uris).let {
            launcher.launch(IntentSenderRequest.Builder(it.intentSender).build())
        }
    }

    /**
     * Requests delete permission for URIs.
     */
    private fun requestDeletePermission(
        launcher: ActivityResultLauncher<IntentSenderRequest>,
        uris: List<Uri>,
        exception: SecurityException
    ) {
        val pendingIntent = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> MediaStore.createDeleteRequest(
                context.contentResolver,
                uris
            )

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && exception is RecoverableSecurityException ->
                exception.userAction.actionIntent

            else -> null
        }
        pendingIntent?.let {
            launcher.launch(IntentSenderRequest.Builder(it.intentSender).build())
        }
    }

    /**
     * Retrieves the MediaStore ID for a media file based on its path and type.
     * @param path The file path of the media (e.g., "/storage/emulated/0/Music/song.mp3").
     * @param mediaType The type of media ("audio", "video", or "image").
     * @return The MediaStore ID as a Long, or 0 if not found or an error occurs.
     */
    private fun Context.findMediaID(path: String, mediaType: String): Long {
        val contentUri = when (mediaType.lowercase()) {
            "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            else -> {
                "getMediaID".log("Unsupported media type: $mediaType")
                return 0
            }
        }

        val projection = arrayOf(MediaStore.MediaColumns._ID)
        val selection = "${MediaStore.MediaColumns.DATA}=?"
        val selectionArgs = arrayOf(path)

        return contentResolver.query(contentUri, projection, selection, selectionArgs, null)
            ?.use { cursor ->
                if (cursor.moveToFirst()) {
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                } else {
                    0
                }
            } ?: 0
    }

    companion object {
        fun with(context: Context): AndroidXI = AndroidXI(context)
    }
}