package cn.martinkay.abcde.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cn.martinkay.abcde.AppState
import cn.martinkay.abcde.ui.theme.Icons
import cn.martinkay.abcde.ui.theme.grayColorFilter
import cn.martinkay.abcde.ui.theme.icon
import cn.martinkay.abcde.util.LazyColumnWithScrollBar
import cn.martinkay.abcde.util.VerticalTabAndContent
import cn.martinkay.abcde.util.composeContent
import cn.martinkay.abcde.util.composeSelectContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.yricky.oh.abcd.cfm.AbcClass
import me.yricky.oh.abcd.cfm.ClassItem
import me.yricky.oh.common.TreeStruct

@Composable
fun ClassListPage(
    modifier: Modifier,
    appState: AppState,
    classList: AppState.ClassList
) {
    val scope = rememberCoroutineScope()
    VerticalTabAndContent(
        modifier,
        listOf(composeSelectContent { _: Boolean ->
            Image(Icons.clazz(), null, Modifier.fillMaxSize(), colorFilter = grayColorFilter)
        } to composeContent {
            Column(Modifier.fillMaxSize()) {
                var filter by remember(classList.filter) {
                    mutableStateOf(classList.filter)
                }
                OutlinedTextField(
                    value = filter,
                    onValueChange = { _filter ->
                        filter = _filter.replace(" ", "").replace("\n", "")
                        scope.launch {
                            if (classList.classList.isNotEmpty()) {
                                delay(500)
                            }
                            if (_filter == filter) {
                                println("Set:$_filter")
                                classList.setNewFilter(filter)
                            } else {
                                println("drop:${_filter}")
                            }
                        }
                    },
                    leadingIcon = {
                        Image(Icons.search(), null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("${classList.classCount}个类")
                    },
                )
                ClassList(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f), classList.classList) {
                    if (it is TreeStruct.LeafNode) {
                        val clazz = it.value
                        if (clazz is AbcClass) {
                            appState.openClass(clazz)
                        }
                    } else if (it is TreeStruct.TreeNode) {
                        classList.toggleExpand(it)
                    }
                }
            }
        }, composeSelectContent {
            Image(Icons.info(), null, Modifier.fillMaxSize(), colorFilter = grayColorFilter)
        } to composeContent {
            Column {
                Text("文件版本:${classList.abc.header.version}")
            }
        }
        ))
}


@Composable
fun ClassList(
    modifier: Modifier,
    classList: List<Pair<Int, TreeStruct.Node<ClassItem>>>,
    onClick: (TreeStruct.Node<ClassItem>) -> Unit = {}
) {
    val state = rememberLazyListState()
    Box(modifier) {
        LazyColumnWithScrollBar(
            Modifier.fillMaxSize(),
            state
        ) {
            items(classList) { clazz ->
                ClassListItem(clazz, onClick)
            }
        }
    }

}

@Composable
fun ClassListItem(
    clazz: Pair<Int, TreeStruct.Node<ClassItem>>,
    onClick: (TreeStruct.Node<ClassItem>) -> Unit
) {
    val density = LocalDensity.current
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick(clazz.second) }
            .drawBehind {
                repeat(clazz.first) {
                    drawRect(
                        Color.hsv(((it * 40) % 360).toFloat(), 1f, 0.5f),
                        topLeft = Offset(density.density * (it * 12 + 5), 0f),
                        size = Size(density.density * 2, size.height)
                    )
                }
            }
            .padding(start = (12 * clazz.first).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val node = clazz.second
        when (node) {
            is TreeStruct.LeafNode<ClassItem> -> {
                Image(node.value.icon(), null, modifier = Modifier
                    .padding(end = 2.dp)
                    .size(16.dp))
            }

            is TreeStruct.TreeNode<ClassItem> -> {
                Image(Icons.pkg(), null, modifier = Modifier
                    .padding(end = 2.dp)
                    .size(16.dp))
            }
        }
        Text(clazz.second.pathSeg, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}