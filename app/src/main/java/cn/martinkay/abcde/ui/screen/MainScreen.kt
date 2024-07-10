package cn.martinkay.abcde.ui.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.martinkay.abcde.AppState
import cn.martinkay.abcde.page.AbcOverviewPage
import cn.martinkay.abcde.page.ClassViewPage
import cn.martinkay.abcde.page.CodeViewPage
import cn.martinkay.abcde.page.WelcomePage
import cn.martinkay.abcde.ui.theme.icon
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController
) {
    ScreenContainer(
        navController,
        title = {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    ) {
                        append("Abcde")
                    }
                }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = null
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ContentUI()
        }
    }
}

@Composable
fun ContentUI() {
    val appState: AppState = remember {
        AppState().apply {

        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        val scrollState = rememberLazyListState()
        val scope = rememberCoroutineScope()
        LazyRow(
            Modifier
                .pointerInput(PointerEventPass.Main) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent(PointerEventPass.Main)
                            if (event.type == PointerEventType.Scroll) {
                                event.changes.lastOrNull()?.scrollDelta?.let {
                                    if (it.x == 0f && scrollState.layoutInfo.totalItemsCount > 1) {
                                        if (it.y > 0) {
                                            scope.launch {
                                                scrollState.animateScrollToItem(
                                                    (scrollState.firstVisibleItemIndex + 1).coerceIn(
                                                        0,
                                                        scrollState.layoutInfo.totalItemsCount
                                                    )
                                                )
                                            }
                                        } else if (it.y < 0) {
                                            scope.launch {
                                                scrollState.animateScrollToItem(
                                                    (scrollState.firstVisibleItemIndex - 1).coerceIn(
                                                        0,
                                                        scrollState.layoutInfo.totalItemsCount
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                .padding(horizontal = 4.dp)
                .padding(top = 4.dp), state = scrollState) {
            item {
                Box(
                    Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .let {
                            if (appState.currPage == null) {
                                it.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            } else it
                        }
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable { appState.currPage = null },
                ) {
                    Image(
                        cn.martinkay.abcde.ui.theme.Icons.homeFolder(),
                        null,
                        modifier = Modifier.align(
                            Alignment.Center
                        )
                    )
                }
            }
            items(appState.pageStack) { p ->
                var hover by remember {
                    mutableStateOf(false)
                }
                Row(
                    Modifier
                        .padding(start = 4.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .pointerInput(PointerEventPass.Main) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent(PointerEventPass.Main)
                                    if (event.type == PointerEventType.Enter) {
                                        hover = true
                                    } else if (event.type == PointerEventType.Exit) {
                                        hover = false
                                    }
                                }
                            }
                        }
                        .let {
                            if (appState.currPage == p) {
                                it.border(
                                    2.dp,
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(14.dp)
                                )
                            } else it
                        }
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable { appState.gotoPage(p) }
                        .padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = if (!hover) {
                            when (p) {
                                is AppState.AbcOverview -> cn.martinkay.abcde.ui.theme.Icons.listFiles()
                                is AppState.ClassView -> p.classItem.icon()
                                is AppState.CodeView -> p.method.icon()
                            }
                        } else {
                            cn.martinkay.abcde.ui.theme.Icons.close()
                        },
                        null,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .clickable {
                                appState.closePage(p)
                            }
                            .padding(6.dp)
                    )
                    val title = remember(p) {
                        p.tag.let {
                            if (it.length > 35) "...${
                                it.substring(
                                    it.length - 32,
                                    it.length
                                )
                            }" else it
                        }
                    }
                    Text(
                        title,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        lineHeight = 14.sp,
                        fontSize = 14.sp,
                        modifier = Modifier,
                        maxLines = 1,
                    )
                }
            }

        }
        Crossfade(appState.currPage) { page ->
            when (page) {
                null -> {
                    WelcomePage(
                        LocalContext.current
                    ) {
                        it?.let { abc -> appState.openAbc(abc) }
                    }
                }

                is AppState.AbcOverview -> AbcOverviewPage(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    appState,
                    page
                )

                is AppState.ClassView -> ClassViewPage(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    appState,
                    page.classItem
                )

                is AppState.CodeView -> CodeViewPage(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                        .padding(bottom = 4.dp),
                    appState,
                    page.method,
                    page.code
                )
            }
        }
    }
}