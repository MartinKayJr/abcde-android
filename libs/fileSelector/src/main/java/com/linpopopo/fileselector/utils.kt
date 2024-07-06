package com.linpopopo.fileselector

import java.io.File

fun Long.byteToCapacity(): String {
    val kb: Long = 1024
    val mb = kb * 1024
    val gb = mb * 1024
    return if (this >= gb) {
        String.format("%.1fGB", this.toFloat() / gb)
    } else if (this >= mb) {
        val f = this.toFloat() / mb
        String.format(if (f > 100) "%.0fMB" else "%.1fMB", f)
    } else if (this > kb) {
        val f = this.toFloat() / kb
        String.format(if (f > 100) "%.0fKB" else "%.1fKB", f)
    } else {
        String.format("%dB", this)
    }
}

fun String.getName() = substringAfterLast(File.separatorChar)

fun File.suffixWith(suffix: String): Boolean {
    if (!this.exists()) return false
    if (!this.isFile) return false
    val fileSuffix = this.canonicalPath.getName().substringAfterLast(".", "")
    return if (suffix.startsWith(".")) {
        fileSuffix == suffix.substring(1)
    } else {
        fileSuffix == suffix
    }
}