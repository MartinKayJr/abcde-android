package com.linpopopo.fileselector

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import java.io.File

@Composable
fun FileItem(
    file: File,
    isSelectFile: Boolean,
    checked: Boolean,
    onFolderClick: () -> Unit,
    onItemCheck: (Boolean) -> Unit
) {
    val fileType = FileTypeUtils.getFileType(file)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (fileType == FileType.DIRECTORY) {
                    onFolderClick.invoke()
                }
            }
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(fileType.icon),
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterVertically),
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterVertically)
                .weight(1f)
        ) {
            Text(
                text = file.name,
                style = MaterialTheme.typography.titleMedium.copy(color = Color.Black)
            )
            if (fileType == FileType.DIRECTORY) {
                val size = file.listFiles()?.size ?: 0
                Text(
                    text = "${stringResource(R.string.text_file)}: $size",
                    style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray)
                )
            } else {
                Text(
                    text = file.length().byteToCapacity(),
                    style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray)
                )
            }
        }

        if (isSelectFile) {
            if (fileType != FileType.DIRECTORY) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = {
                        onItemCheck.invoke(it)
                    },
                    enabled = true,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Green
                    )
                )
            }
        } else {
            Checkbox(
                checked = checked,
                onCheckedChange = {
                    onItemCheck.invoke(it)
                },
                enabled = true,
                modifier = Modifier.align(Alignment.CenterVertically),
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Green
                )
            )
        }
    }
}