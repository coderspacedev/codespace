package coder.apps.space.library.extension

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Copies a single file from source to target.
 *
 * @param source The source file to copy.
 * @param target The target file to copy to.
 * @param onProgress Optional callback that receives progress percentage (0-100).
 * @param onComplete Optional callback invoked when copy completes successfully.
 * @param onError Optional callback invoked with an error message if copying fails.
 */
fun copyFile(
    source: File,
    target: File,
    onProgress: ((Int) -> Unit)? = null,
    onComplete: (() -> Unit)? = null,
    onError: ((String) -> Unit)? = null,
) {
    try {
        val totalSize = source.length()
        var copied = 0L

        val input = FileInputStream(source)
        val output = FileOutputStream(target)
        val buffer = ByteArray(8192)
        var read: Int

        while (input.read(buffer).also { read = it } != -1) {
            output.write(buffer, 0, read)
            copied += read
            val progress = ((copied * 100) / totalSize).toInt()
            onProgress?.invoke(progress)
        }

        input.close()
        output.flush()
        output.close()
        onComplete?.invoke()
    } catch (e: Exception) {
        onError?.invoke(e.message ?: "Copy failed")
    }
}

/**
 * Copies multiple files into the target directory asynchronously.
 *
 * @param files List of files to copy.
 * @param targetDir Destination directory where files will be copied.
 * @param onProgress Optional callback with total progress percentage (0-100) across all files.
 * @param onComplete Optional callback invoked when all files are copied.
 * @param onError Optional callback invoked with an error message if any copy fails.
 */
fun copyFiles(
    files: List<File>,
    targetDir: File,
    onProgress: ((Int) -> Unit)? = null,
    onComplete: (() -> Unit)? = null,
    onError: ((String) -> Unit)? = null,
) {
    Thread {
        try {
            var totalCopied = 0
            for ((index, file) in files.withIndex()) {
                val target = File(targetDir, file.name)
                copyFile(
                    file, target,
                    onProgress = { progress ->
                        val totalProgress = ((index + progress / 100.0) / files.size * 100).toInt()
                        onProgress?.invoke(totalProgress)
                    },
                    onComplete = {
                        totalCopied++
                        if (totalCopied == files.size) {
                            onComplete?.invoke()
                        }
                    },
                    onError = { error ->
                        onError?.invoke(error)
                    }
                )
            }
        } catch (e: Exception) {
            onError?.invoke(e.message ?: "Multiple copy failed")
        }
    }.start()
}

/**
 * Moves a single file from source to target by copying then deleting the original.
 *
 * @param source The source file to move.
 * @param target The target file destination.
 * @param onProgress Optional callback that receives progress percentage (0-100) during copy.
 * @param onComplete Optional callback invoked when move completes successfully.
 * @param onError Optional callback invoked with an error message if move fails.
 */
fun moveFile(
    source: File,
    target: File,
    onProgress: ((Int) -> Unit)? = null,
    onComplete: (() -> Unit)? = null,
    onError: ((String) -> Unit)? = null,
) {
    copyFile(
        source, target,
        onProgress = onProgress,
        onComplete = {
            if (source.delete()) {
                onComplete?.invoke()
            } else {
                onError?.invoke("Failed to delete original file after move")
            }
        },
        onError = onError
    )
}

/**
 * Moves multiple files to the target directory asynchronously.
 *
 * @param files List of files to move.
 * @param targetDir Destination directory.
 * @param onProgress Optional callback with total move progress (0-100).
 * @param onComplete Optional callback when all files are moved.
 * @param onError Optional callback for any error.
 */
fun moveFiles(
    files: List<File>,
    targetDir: File,
    onProgress: ((Int) -> Unit)? = null,
    onComplete: (() -> Unit)? = null,
    onError: ((String) -> Unit)? = null,
) {
    Thread {
        try {
            var moved = 0
            for ((index, file) in files.withIndex()) {
                val target = File(targetDir, file.name)
                moveFile(
                    file, target,
                    onProgress = { progress ->
                        val totalProgress = ((index + progress / 100.0) / files.size * 100).toInt()
                        onProgress?.invoke(totalProgress)
                    },
                    onComplete = {
                        moved++
                        if (moved == files.size) {
                            onComplete?.invoke()
                        }
                    },
                    onError = { error ->
                        onError?.invoke(error)
                    })
            }
        } catch (e: Exception) {
            onError?.invoke(e.message ?: "Move failed")
        }
    }.start()
}

/**
 * Renames a file synchronously.
 *
 * @param file The file to rename.
 * @param newName The new name for the file.
 * @param onComplete Optional callback when rename succeeds.
 * @param onError Optional callback if rename fails.
 */
fun renameFile(
    file: File,
    newName: String,
    onComplete: (() -> Unit)? = null,
    onError: ((String) -> Unit)? = null,
) {
    try {
        val newFile = File(file.parent, newName)
        if (file.renameTo(newFile)) {
            onComplete?.invoke()
        } else {
            onError?.invoke("Rename failed")
        }
    } catch (e: Exception) {
        onError?.invoke(e.message ?: "Rename failed")
    }
}

/**
 * Deletes a single file synchronously.
 *
 * @param file The file to delete.
 * @param onComplete Optional callback when delete succeeds.
 * @param onError Optional callback if delete fails.
 */
fun deleteFile(
    file: File,
    onComplete: (() -> Unit)? = null,
    onError: ((String) -> Unit)? = null,
) {
    try {
        if (file.delete()) {
            onComplete?.invoke()
        } else {
            onError?.invoke("Delete failed")
        }
    } catch (e: Exception) {
        onError?.invoke(e.message ?: "Delete failed")
    }
}

/**
 * Deletes multiple files asynchronously.
 *
 * @param files List of files to delete.
 * @param onProgress Optional callback with deletion progress (0-100).
 * @param onComplete Optional callback when all files deleted.
 * @param onError Optional callback if any deletion fails.
 */
fun deleteFiles(
    files: List<File>,
    onProgress: ((Int) -> Unit)? = null,
    onComplete: (() -> Unit)? = null,
    onError: ((String) -> Unit)? = null,
) {
    Thread {
        try {
            var deleted = 0
            for (file in files) {
                if (file.delete()) {
                    deleted++
                    val progress = (deleted * 100) / files.size
                    onProgress?.invoke(progress)
                } else {
                    onError?.invoke("Failed to delete ${file.name}")
                    return@Thread
                }
            }
            onComplete?.invoke()
        } catch (e: Exception) {
            onError?.invoke(e.message ?: "Delete failed")
        }
    }.start()
}