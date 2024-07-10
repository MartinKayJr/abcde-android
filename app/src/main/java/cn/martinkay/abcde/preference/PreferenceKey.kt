package cn.martinkay.abcde.preference

import android.os.Build
import io.github.rosemoe.sora.widget.CodeEditor

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class PreferenceKeyName

enum class PreferenceKey(
    @PreferenceKeyName val keyName: String,
    val defaultValue: Any,
) {

    // App
    AppThemeMode(keyName = "pref_app_theme_mode", defaultValue = PreferenceEnum.Theme.System.name),
    AppDynamicColor(keyName = "pref_app_dynamic_color_enabled", defaultValue = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S),

    // Editor
    EditorFontSize(keyName = "pref_editor_font_size", defaultValue = CodeEditor.DEFAULT_TEXT_SIZE.toFloat())

}