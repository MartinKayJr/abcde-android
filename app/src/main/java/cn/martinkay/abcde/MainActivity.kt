package cn.martinkay.abcde

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.martinkay.abcde.bean.menu.MenuGroup
import cn.martinkay.abcde.bean.menu.MenuItem
import cn.martinkay.abcde.page.ClassListPage
import cn.martinkay.abcde.page.ClassViewPage
import cn.martinkay.abcde.page.CodeViewPage
import cn.martinkay.abcde.page.WelcomePage
import cn.martinkay.abcde.ui.theme.AbcdeTheme
import cn.martinkay.abcde.ui.theme.Icons
import cn.martinkay.abcde.ui.theme.icon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AbcdeTheme {
                Surface {
                    DrawerUI()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerUI() {
    //抽屉展开关闭状态
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerHeader()
                DrawerBody(groups = listOf(
                    MenuGroup(
                        id = "main-group",
                        name = "功能",
                        items = listOf(
                            MenuItem(
                                id = "home",
                                title = "home",
                                contentDescription = "go to home screen",
                                icon = androidx.compose.material.icons.Icons.Default.Home
                            ),
                        )
                    ),
                    MenuGroup(
                        id = "other-group",
                        name = "其他",
                        items = listOf(
                            MenuItem(
                                id = "settings",
                                title = "Settings",
                                contentDescription = "go to settings screen",
                                icon = androidx.compose.material.icons.Icons.Default.Settings
                            ),
                            MenuItem(
                                id = "help",
                                title = "Help",
                                contentDescription = "go to help screen",
                                icon = androidx.compose.material.icons.Icons.Default.Info
                            )
                        )
                    )
                ),
                    onItemClick = {
                        when (it.id) {
                            "setting" -> {}
                        }
                        println("Clicked on ${it.title}")
                    }
                )
            }
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
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
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }) {
                            Icon(
                                androidx.compose.material.icons.Icons.Outlined.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                // 悬浮按钮
                FloatingActionButton(onClick = { /*TODO*/ }) {
                    Icon(
                        androidx.compose.material.icons.Icons.Outlined.Menu,
                        contentDescription = "Menu"
                    )
                }
            },
            content = { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    ContentUI()
                }
            }
        )
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
                    Image(Icons.homeFolder(), null, modifier = Modifier.align(Alignment.Center))
                }
            }
            items(appState.pageStack) { p ->
                var hover by remember {
                    mutableStateOf(false)
                }
                Row(
                    Modifier
                        .padding(start = 4.dp)
                        .height(28.dp)
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
                                is AppState.ClassList -> Icons.listFiles()
                                is AppState.ClassView -> p.classItem.icon()
                                is AppState.CodeView -> p.method.icon()
                            }
                        } else {
                            Icons.close()
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

                is AppState.ClassList -> ClassListPage(
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AbcdeTheme {
        Surface {
            DrawerUI()
        }
    }
}