package com.linpopopo.fileselector

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileSelectorAppBar(
    fileSelectorData: FileSelectorData,
    currentFolder: File,
    currentPath: String,
    dropdownState: Boolean,
    checkBoxStateMap: Map<File, Boolean>,
    onCurrentPathChanged: (String) -> Unit,
    onClose: () -> Unit,
    onConfirm: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = currentFolder.name)
                if (dropdownState) {
                    Column {
                        var expanded by remember { mutableStateOf(false) }
                        IconButton(
                            onClick = { expanded = true }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            val paths = mutableListOf<String>()
                            paths.add(fileSelectorData.rootPath)
                            val middlePaths = mutableListOf<String>()
                            var parentPath = File(currentPath).parent
                            while (parentPath != null && parentPath != fileSelectorData.rootPath) {
                                middlePaths.add(parentPath)
                                parentPath = File(parentPath).parent
                            }
                            paths.addAll(middlePaths.reversed())
                            paths.add(currentPath)
                            paths.forEachIndexed { _, s ->
                                DropdownMenuItem(text = {
                                    Text(text = s.getName())
                                }, onClick = {
                                    onCurrentPathChanged.invoke(s)
                                    expanded = false
                                })
                            }
                        }
                    }
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        },
        actions = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.Green
                ),
                shape = ShapeDefaults.ExtraSmall,
                enabled = !checkBoxStateMap.all { !it.value }
            ) {
                Text(text = stringResource(id = R.string.text_confirm))
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        ),
    )
}