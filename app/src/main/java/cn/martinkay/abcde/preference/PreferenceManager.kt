package cn.martinkay.abcde.preference

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import me.zhanghai.compose.preference.rememberPreferenceState

object PreferenceManager {

    @Composable
    fun <T> String.getPreferenceValue(defaultValue: T): T {
        return rememberPreferenceState(key = this, defaultValue = defaultValue).value
    }

    @Composable
    private fun getThemeMode(theme: String) = when (theme) {
        PreferenceEnum.Theme.System.name -> isSystemInDarkTheme()
        PreferenceEnum.Theme.Dark.name -> true
        PreferenceEnum.Theme.Light.name -> false
        else -> throw Exception("Unknown Theme: $theme")
    }

    @Composable
    fun getThemeMode() = getThemeMode(
        PreferenceKey.AppThemeMode.keyName.getPreferenceValue(
            defaultValue = PreferenceKey.AppThemeMode.defaultValue.toString()
        )
    )

    @Composable
    fun isDynamicColorEnabled() = PreferenceKey.AppDynamicColor.keyName.getPreferenceValue(
        defaultValue = PreferenceKey.AppDynamicColor.defaultValue
    ) as Boolean

    @Composable
    fun getEditorFontSize() = PreferenceKey.EditorFontSize.keyName.getPreferenceValue(
        defaultValue = PreferenceKey.EditorFontSize.defaultValue
    ).toString().toFloat()

}