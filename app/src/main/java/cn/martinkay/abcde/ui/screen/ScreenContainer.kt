package cn.martinkay.abcde.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import cn.martinkay.abcde.DrawerBody
import cn.martinkay.abcde.DrawerHeader
import cn.martinkay.abcde.ScreenRoute
import cn.martinkay.abcde.bean.menu.MenuGroup
import cn.martinkay.abcde.bean.menu.MenuItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.zhanghai.compose.preference.ProvidePreferenceTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContainer(
    navController: NavController,
    title: @Composable () -> Unit,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary
    ),
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val windowInsets = WindowInsets.safeDrawing
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    //抽屉展开关闭状态
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    ProvidePreferenceTheme {
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
                            // 如果当前页面不是Main，则popBackStack
                            if (navController.currentDestination?.route != ScreenRoute.Main.name) {
                                navController.popBackStack()
                            }
                            when (it.id) {
                                "home" -> {
                                    navController.navigate(ScreenRoute.Main.name)
                                }
                                "settings" -> {
                                    navController.navigate(ScreenRoute.Settings.name)
                                }
                                "help" -> {
                                    navController.navigate(ScreenRoute.Help.name)
                                }
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
                        title = { title() },
                        navigationIcon = {
                            // 如果有navigationIcon则显示，没有则
                            if (navigationIcon != null) {
                                navigationIcon.invoke()
                            } else {
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
                        },
                        actions = { actions?.invoke() },
                        windowInsets = windowInsets.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
                        colors = colors
                    )
                },
                containerColor = Color.Transparent,
                contentColor = contentColorFor(MaterialTheme.colorScheme.background),
            ) {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                ) {
                    content()
                }
            }
        }
    }

}