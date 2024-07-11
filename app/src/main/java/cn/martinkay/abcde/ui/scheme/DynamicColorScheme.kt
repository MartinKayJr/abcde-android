package cn.martinkay.abcde.ui.scheme

import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

class DynamicEditorColorScheme(
    handleColor: Color?,
    selectionBackgroundColor: Color?,
    private val colorScheme: ColorScheme?
) : BaseEditorColorScheme(handleColor, selectionBackgroundColor) {

    init {
        applyDefault()
    }

    override fun applyDefault() {
        if (
            colorScheme == null
        ) {
            return
        }
        super.applyDefault()

        setColor(WHOLE_BACKGROUND, colorScheme.inverseOnSurface.copy(alpha = 0.25f).toArgb())
        setColor(LINE_NUMBER_BACKGROUND, colorScheme.inverseOnSurface.copy(alpha = 0.6f).toArgb())
        setColor(LINE_NUMBER, colorScheme.onSurfaceVariant.copy(alpha = 0.8f).toArgb())
        setColor(LINE_NUMBER_CURRENT, colorScheme.primary.toArgb())
        setColor(LINE_DIVIDER, Color.Transparent.toArgb())
        setColor(SELECTION_HANDLE, handleColor!!.toArgb())
        setColor(SELECTION_INSERT, handleColor.toArgb())
        setColor(SELECTED_TEXT_BACKGROUND, selectionBackgroundColor!!.toArgb())
        setColor(TEXT_NORMAL, colorScheme.onSurfaceVariant.toArgb())
        setColor(CURRENT_LINE, colorScheme.inverseOnSurface.copy(alpha = 0.8f).toArgb())
        setColor(SCROLL_BAR_TRACK, colorScheme.onSurfaceVariant.copy(alpha = 0.08f).toArgb())
        setColor(SCROLL_BAR_THUMB, colorScheme.onSurfaceVariant.copy(alpha = 0.2f).toArgb())
        setColor(SCROLL_BAR_THUMB_PRESSED, colorScheme.onSurfaceVariant.copy(alpha = 0.4f).toArgb())
        setColor(LINE_NUMBER_PANEL, Color.Black.copy(alpha = 0.6f).toArgb())
        setColor(LINE_NUMBER_PANEL_TEXT, Color.White.toArgb())
    }

}