package cn.martinkay.abcde.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import cn.martinkay.abcde.AppState
import cn.martinkay.abcde.R
import cn.martinkay.abcde.content.ModuleInfoContent
import cn.martinkay.abcde.ui.theme.Icons
import cn.martinkay.abcde.ui.theme.defineStr
import cn.martinkay.abcde.ui.theme.grayColorFilter
import cn.martinkay.abcde.util.LazyColumnWithScrollBar
import cn.martinkay.abcde.util.VerticalTabAndContent
import cn.martinkay.abcde.util.composeContent
import cn.martinkay.abcde.util.composeSelectContent
import me.yricky.oh.abcd.cfm.AbcClass
import me.yricky.oh.abcd.cfm.AbcMethod
import me.yricky.oh.abcd.cfm.FieldType
import me.yricky.oh.abcd.code.Code
import me.yricky.oh.abcd.code.TryBlock
import me.yricky.oh.abcd.isa.Asm

val CODE_FONT = FontFamily(Font(R.font.jetbrains_mono_regular))
val commentColor = Color(0xff72737a)
val codeStyle
    @Composable get() = TextStyle(
        fontFamily = CODE_FONT,
        color = Color(0xffa9b7c6),
        fontSize = MaterialTheme.typography.bodyMedium.fontSize
    )


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CodeViewPage(modifier: Modifier, appState: AppState, method: AbcMethod, code: Code?) {
    VerticalTabAndContent(modifier, listOfNotNull(
        code?.let {
            composeSelectContent { _: Boolean ->
                Image(Icons.asm(), null, Modifier.fillMaxSize())
            } to composeContent {
                Column(Modifier.fillMaxSize()) {
                    Text(
                        "寄存器数量:${code.numVRegs}, 参数数量:${code.numArgs}, 指令字节数:${code.codeSize}, TryCatch数:${code.triesSize}",
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
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
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                2.dp,
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    ) {
//                        FixedSelectionContainer {
                        var tryBlock by remember {
                            mutableStateOf<TryBlock?>(null)
                        }
                        LazyColumnWithScrollBar {
                            item {
                                Text(method.defineStr(true), style = codeStyle)
                            }
                            itemsIndexed(code.asm.list) { index, item ->
                                Row {
                                    item.asm
                                    DisableSelection {
                                        val line = remember {
                                            "$index ".let {
                                                "${" ".repeat((5 - it.length).coerceAtLeast(0))}$it"
                                            }
                                        }
                                        Text(line, style = codeStyle)
                                    }
//                                    DisableSelection {
                                        val tb = remember(item) { item.tryBlocks }
//                                        ContextMenuArea(
//                                            items = {
//                                                buildList<ContextMenuItem> {
//                                                    if (tryBlock != null) {
//                                                        add(ContextMenuItem("隐藏行高亮") {
//                                                            tryBlock = null
//                                                        })
//                                                    }
//                                                    item.asm
//                                                    it.tryBlocks.forEach {
//                                                        add(
//                                                            ContextMenuItem(
//                                                                "高亮 TryBlock[0x${
//                                                                    it.startPc.toString(
//                                                                        16
//                                                                    )
//                                                                },0x${
//                                                                    (it.startPc + it.length).toString(
//                                                                        16
//                                                                    )
//                                                                }]"
//                                                            ) {
//                                                                tryBlock = it
//                                                            }
//                                                        )
//                                                    }
//                                                    item.calledMethods.forEach {
//                                                        add(ContextMenuItem("跳转到${it.name}") {
//                                                            appState.openCode(it)
//                                                        })
//                                                    }
//                                                }
//                                            }
//                                        ) {
                                            Text(
                                                String.format("%04X ", item.codeOffset),
                                                style = codeStyle.copy(color = commentColor),
                                                modifier = with(Modifier) {
                                                    val density = LocalDensity.current
                                                    if (tryBlock != null) {
                                                        drawBehind {
                                                            if (tb.contains(tryBlock)) {
                                                                drawRect(
                                                                    Color.Yellow,
                                                                    size = Size(
                                                                        density.density * 2,
                                                                        size.height
                                                                    )
                                                                )
                                                            }
                                                        }
                                                    } else this
                                                }.let { m ->
                                                    if (tryBlock?.catchBlocks?.find { item.codeOffset in (it.handlerPc until (it.handlerPc + it.codeSize)) } != null) {
                                                        m.background(MaterialTheme.colorScheme.errorContainer)
                                                    } else {
                                                        m
                                                    }
                                                }
                                            )
//                                        }
//                                    }
//                                    TooltipArea(tooltip = {
//                                        DisableSelection {
//                                            Surface(
//                                                shape = MaterialTheme.shapes.medium,
//                                                color = MaterialTheme.colorScheme.primaryContainer
//                                            ) {
//                                                Column(modifier = Modifier.padding(8.dp)) {
//                                                    Text(
//                                                        item.ins.instruction.sig,
//                                                        style = codeStyle
//                                                    )
//                                                    if (item.ins.instruction.properties != null) {
//                                                        Text(
//                                                            "prop:${item.ins.instruction.properties}",
//                                                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
//                                                        )
//                                                    }
//                                                    Text(
//                                                        "组:${item.ins.group.title}",
//                                                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
//                                                    )
//                                                    Text(
//                                                        "组描述:${item.ins.group.description.trim()}",
//                                                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
//                                                    )
//                                                }
//                                            }
//                                        }
//                                    }, modifier = Modifier.fillMaxSize()) {
                                        Text(
                                            text = asmString[item]!!,
                                            style = codeStyle,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Text("\n", maxLines = 1, style = codeStyle)
//                                    }
                                }
                            }
                            item {
                                Spacer(Modifier.height(120.dp))
                            }
//                            }
                        }
                        val clipboardManager = LocalClipboardManager.current
                        FloatingActionButton(
                            {
                                clipboardManager.setText(AnnotatedString(asmString.values.fold("\n") { s, i ->
                                    "$s\n${i}"
                                }))
                            }, modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp)
                        ) {
                            Text("复制")
                        }
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