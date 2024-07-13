package cn.martinkay.abcde.page.editor

import android.graphics.Typeface
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import cn.martinkay.abcde.page.commentColor
import cn.martinkay.abcde.preference.PreferenceManager
import cn.martinkay.abcde.ui.component.CodeEditor
import cn.martinkay.abcde.ui.scheme.DynamicEditorColorScheme
import cn.martinkay.abcde.ui.theme.defineStr
import me.yricky.oh.abcd.cfm.AbcMethod
import me.yricky.oh.abcd.code.Code
import me.yricky.oh.abcd.isa.Asm

@Composable
fun SoraCodeEditor(
    method: AbcMethod,
    code: Code
) {

    // 将代码编辑器的内容显示在这里
    val defineStr = method.defineStr(true)
    val fontSize = PreferenceManager.getEditorFontSize()
    val sb = StringBuilder()
    val asmString: Map<Asm.AsmItem, AnnotatedString> = remember {
        code.asm.list.associateWith {
            buildAnnotatedString {
                append(buildAnnotatedString {
                    val asmName = it.asmName
                    append(asmName)
                    addStyle(
                        SpanStyle(Color(0xff9876aa)),
                        0,
                        asmName.length
                    )
                })
                append(' ')
                append(buildAnnotatedString {
                    append(it.asmArgs)
                })
                append("    ")
                append(buildAnnotatedString {
                    val asmComment = it.asmComment
                    append(asmComment)
                    addStyle(
                        SpanStyle(commentColor),
                        0,
                        asmComment.length
                    )
                })
            }
        }
    }
    code.asm.list.forEach {
        sb.append(asmString[it])
        sb.append("\n")
    }
    CodeEditor(
        colorScheme = DynamicEditorColorScheme(
            colorScheme = MaterialTheme.colorScheme,
            handleColor = LocalTextSelectionColors.current.handleColor,
            selectionBackgroundColor = LocalTextSelectionColors.current.backgroundColor
        ),
        properties = {
            it.apply {
                val font = Typeface.createFromAsset(context.assets, "fonts/JetBrainsMono.ttf")
                typefaceText = font
                typefaceLineNumber = font

                setDividerMargin(30f, 0f)
                lineNumberMarginLeft = 30f

                setTextSize(fontSize.toFloat())
                setText(sb.toString())
            }
        },

    )

}