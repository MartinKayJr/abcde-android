package cn.martinkay.abcde

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.martinkay.abcde.ui.screen.AboutScreen
import cn.martinkay.abcde.ui.screen.MainScreen
import cn.martinkay.abcde.ui.screen.SettingsScreen

enum class ScreenRoute {
    Main, Settings, Help
}

@Composable
fun App() {
    ScreenNavController()
}

@Composable
fun ScreenNavController() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.Main.name
    ) {
        composable(ScreenRoute.Main.name) { MainScreen(navController) }
        composable(ScreenRoute.Settings.name) { SettingsScreen(navController) }
        composable(ScreenRoute.Help.name) { AboutScreen(navController) }
    }
}