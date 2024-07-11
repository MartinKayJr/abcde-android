package cn.martinkay.abcde.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import cn.martinkay.abcde.AppState
import cn.martinkay.abcde.R
import cn.martinkay.abcde.content.ModuleInfoContent
import cn.martinkay.abcde.page.editor.DefaultCodeEditor
import cn.martinkay.abcde.page.editor.SoraCodeEditor
import cn.martinkay.abcde.preference.PreferenceManager
import cn.martinkay.abcde.ui.theme.Icons
import cn.martinkay.abcde.ui.theme.grayColorFilter
import cn.martinkay.abcde.util.LazyColumnWithScrollBar
import cn.martinkay.abcde.util.VerticalTabAndContent
import cn.martinkay.abcde.util.composeContent
import cn.martinkay.abcde.util.composeSelectContent
import me.yricky.oh.abcd.cfm.AbcClass
import me.yricky.oh.abcd.cfm.AbcMethod
import me.yricky.oh.abcd.cfm.FieldType
import me.yricky.oh.abcd.code.Code

val CODE_FONT = FontFamily(Font(R.font.jetbrains_mono_regular))
val commentColor = Color(0xff72737a)
val codeStyle
    @Composable get() = TextStyle(
        fontFamily = CODE_FONT,
        color = Color(0xffa9b7c6),
        fontSize = MaterialTheme.typography.bodyMedium.fontSize
    )


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CodeViewPage(modifier: Modifier, appState: AppState, method: AbcMethod, code: Code?) {
    VerticalTabAndContent(modifier, listOfNotNull(
        code?.let {
            composeSelectContent { _: Boolean ->
                Image(Icons.asm(), null, Modifier.fillMaxSize())
            } to composeContent {
                val codeEditorChoose = PreferenceManager.getCodeEditorChoose()
                when (codeEditorChoose) {
                    "sora" -> {
                        SoraCodeEditor(method = method, code = code)
                    }

                    "default" -> {
                        DefaultCodeEditor(method = method, code = code)
                    }

                    else -> {
                        DefaultCodeEditor(method = method, code = code)
                    }
                }
            }
        }, composeSelectContent { _: Boolean ->
            Image(
                Icons.listFiles(),
                null,
                Modifier
                    .fillMaxSize()
                    .alpha(0.5f),
                colorFilter = grayColorFilter
            )
        } to composeContent {
            Column(Modifier.fillMaxSize()) {
                LazyColumnWithScrollBar {
                    items(method.data) {
                        Text("$it")
                    }
                }
            }
        }, (method.clazz as? FieldType.ClassType)?.let { it.clazz as? AbcClass }?.let { clazz ->
            composeSelectContent { _: Boolean ->
                Image(
                    Icons.pkg(),
                    null,
                    Modifier
                        .fillMaxSize()
                        .alpha(0.5f),
                    colorFilter = grayColorFilter
                )
            } to composeContent {
                ModuleInfoContent(Modifier.fillMaxSize(), clazz)
            }
        }
    ))
}