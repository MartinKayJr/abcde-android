package com.linpopopo.fileselector

import android.webkit.MimeTypeMap
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

object FileTypeUtils {
    private val fileTypeExtensions = hashMapOf<String, FileType>()

    init {
        for (fileType in FileType.values()) {
            for (extension in fileType.extensions) {
                fileTypeExtensions[extension] = fileType
            }
        }
    }

    fun getFileType(file: File): FileType {
        if (file.isDirectory) {
            return FileType.DIRECTORY
        }
        return fileTypeExtensions[getExtension(file.name)] ?: FileType.UNKNOWN
    }

    private fun getExtension(fileName: String): String {
        val encoded = try {
            URLEncoder.encode(fileName, "UTF-8").replace("+", "%20")
        } catch (e: UnsupportedEncodingException) {
            fileName
        }
        return MimeTypeMap.getFileExtensionFromUrl(encoded).lowercase()
    }

}