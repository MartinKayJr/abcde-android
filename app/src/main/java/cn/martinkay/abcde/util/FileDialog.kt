package cn.martinkay.abcde.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.linpopopo.fileselector.FileSelector
import com.linpopopo.fileselector.FileSelectorData

@Composable
fun FileSelectorDialog(
    fileSelectorData: FileSelectorData,
    onDismissRequest: () -> Unit,
    onClose: () -> Unit,
    onSelectedPaths: (List<String>) -> Unit
) {
    ColumnDialog(
        onDismissRequest = onDismissRequest
    ) {
        FileSelector(
            fileSelectorData,
            onClose = onClose,
            onSelectedPaths = onSelectedPaths
        )
    }
}


@Composable
fun ColumnDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    padding: Dp = 8.dp,
    background: Color = Color.White,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Column(
            modifier = Modifier
                .background(background)
                .padding(padding),
            content = content
        )
    }
}