package cn.martinkay.abcde.page.editor

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import cn.martinkay.abcde.page.codeStyle
import cn.martinkay.abcde.page.commentColor
import cn.martinkay.abcde.ui.theme.defineStr
import cn.martinkay.abcde.util.LazyColumnWithScrollBar
import me.yricky.oh.abcd.cfm.AbcMethod
import me.yricky.oh.abcd.code.Code
import me.yricky.oh.abcd.code.TryBlock
import me.yricky.oh.abcd.isa.Asm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultCodeEditor(
    method: AbcMethod,
    code: Code
) {
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
            var tryBlock by remember {
                mutableStateOf<TryBlock?>(null)
            }
            val tooltipState = remember { mutableStateOf(false) }
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
                        val tb = remember(item) { item.tryBlocks }
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
                            },
                        )
                        if (tooltipState.value) {
                            RichTooltip(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                DisableSelection {
                                    Surface(
                                        shape = MaterialTheme.shapes.medium,
                                        color = MaterialTheme.colorScheme.primaryContainer
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Text(
                                                item.ins.instruction.sig,
                                                style = codeStyle
                                            )
                                            if (item.ins.instruction.properties != null) {
                                                Text(
                                                    "prop:${item.ins.instruction.properties}",
                                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                                                )
                                            }
                                            Text(
                                                "组:${item.ins.group.title}",
                                                fontSize = MaterialTheme.typography.bodyMedium.fontSize
                                            )
                                            Text(
                                                "组描述:${item.ins.group.description.trim()}",
                                                fontSize = MaterialTheme.typography.bodyMedium.fontSize
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Text(
                            text = asmString[item]!!,
                            style = codeStyle,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text("\n", maxLines = 1, style = codeStyle)
                    }
                }
                item {
                    Spacer(Modifier.height(120.dp))
                }
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