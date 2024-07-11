package cn.martinkay.abcde.ui.component

import android.view.MotionEvent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import io.github.rosemoe.sora.widget.CodeEditor
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme

@Composable
fun CodeEditor(
    modifier: Modifier = Modifier,
    colorScheme: EditorColorScheme = EditorColorScheme(),
    properties: ((CodeEditor) -> Unit)? = null
) {
    val context = LocalContext.current
    val editor = remember {
        CodeEditor(context)
            .also {
                it.colorScheme = colorScheme
                properties?.invoke(it)
            }
    }
    AndroidView(
        factory = {
            editor
        },
        modifier = modifier.fillMaxSize(),
        onRelease = { it.release() }
    )
}