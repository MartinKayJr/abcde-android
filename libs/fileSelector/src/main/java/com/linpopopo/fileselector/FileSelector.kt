package com.linpopopo.fileselector

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import java.io.File

@Composable
fun FileSelector(
    fileSelectorData: FileSelectorData,
    onClose: () -> Unit,
    onSelectedPaths: (List<String>) -> Unit
) {

    var currentPath by remember { mutableStateOf(fileSelectorData.rootPath) }
    val currentFolder = File(currentPath)

    val dropdownState = currentPath != fileSelectorData.rootPath
    val files = currentFolder.listFiles()?.toList()?.sortedWith { f1, f2 ->
        if (f1 == f2) {
            return@sortedWith 0
        }
        if (f1.isDirectory && f2.isFile) {
            return@sortedWith -1
        }
        if (f1.isFile && f2.isDirectory) {
            return@sortedWith 1
        }
        return@sortedWith f1.name.compareTo(f2.name, ignoreCase = true)
    }
    val checkBoxState = remember { mutableStateMapOf<File, Boolean>() }
    files?.filter { if (fileSelectorData.isSelectFile) it.isFile else it.isDirectory }?.forEach { checkBoxState[it] = false }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        FileSelectorAppBar(
            fileSelectorData = fileSelectorData,
            currentFolder = currentFolder,
            currentPath = currentPath,
            dropdownState = dropdownState,
            checkBoxStateMap = checkBoxState,
            onCurrentPathChanged = { currentPath = it },
            onClose = onClose,
            onConfirm = {
                onSelectedPaths.invoke(checkBoxState.filter { it.value }.keys.map { it.absolutePath })
            }
        )
        if (!currentFolder.exists() || !currentFolder.isDirectory) {
            Text(
                text = stringResource(R.string.text_selected_directory_absent),
                style = MaterialTheme.typography.labelMedium.copy(color = Color.Red),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            return@Column
        }

        if (files.isNullOrEmpty()) {
            Text(
                text = stringResource(id = R.string.text_selected_folder_empty, currentFolder.name),
                style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray),
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        } else {
            FileSelectorContent(
                isSelectFile = fileSelectorData.isSelectFile,
                fileType = fileSelectorData.fileType,
                files = files,
                fileItemChecked = {
                    checkBoxState[it] ?: false
                },
                onFolderClick = {
                    currentPath = it.absolutePath
                    checkBoxState.clear()
                },
                onItemCheck = { file: File, b: Boolean ->
                    checkBoxState[file] = b
                    if (!fileSelectorData.isMultiple) {
                        if (b) {
                            checkBoxState.filter { entry ->
                                entry.key != file && entry.value
                            }.keys.forEach {
                                checkBoxState[it] = false
                            }
                        }
                    }
                })
        }
    }
}

@Composable
fun ColumnScope.FileSelectorContent(
    isSelectFile: Boolean,
    files: List<File>,
    fileItemChecked: (File) -> Boolean,
    onFolderClick: (File) -> Unit,
    onItemCheck: (File, Boolean) -> Unit,
    fileType: String = "",
) {
    val allFile = if (isSelectFile) {
        if (fileType.isNotEmpty()) {
            files.filter { it.isDirectory || (it.isFile && it.suffixWith(fileType)) }
        } else {
            files
        }
    } else {
        files.filter { it.isDirectory }
    }

    if (allFile.isEmpty()) {
        Text(
            text = stringResource(id = R.string.text_selected_file_or_folder_empty),
            style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        return
    }

    LazyColumn(
        modifier = Modifier.padding(smallPadding)
    ) {
        itemsIndexed(allFile) { _, item ->
            FileItem(
                file = item,
                isSelectFile = isSelectFile,
                checked = fileItemChecked(item),
                onFolderClick = { onFolderClick(item) },
                onItemCheck = { onItemCheck(item, it) }
            )
        }
    }
}