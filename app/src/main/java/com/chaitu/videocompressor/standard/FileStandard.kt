package com.chaitu.videocompressor.standard

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import java.io.File

fun Context.getTargetPath(folderName: String, fileName: String): String {
    val dir = File(
        filesDir,
        folderName
    )
    if (!dir.exists()) {
        dir.mkdir()
    }

    val file = File(dir, fileName)

    return file.absolutePath
}

fun Context.getAbsolutePathFromUri(uri: Uri): String {
    var cursor: Cursor? = null
    try {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.let { cursor ->
            val pathIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(pathIndex)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    } finally {
        cursor?.run { close() }
    }
    return ""
}

fun String.getFileNameFromAbsolutePath() = substring(lastIndexOf("/") + 1, length)
