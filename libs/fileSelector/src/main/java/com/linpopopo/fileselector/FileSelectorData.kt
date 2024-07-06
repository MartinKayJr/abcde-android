package com.linpopopo.fileselector

data class FileSelectorData(
    val rootPath: String,
    val isSelectFile: Boolean = true,
    val isMultiple: Boolean = false,
    val fileType: String = ""
)