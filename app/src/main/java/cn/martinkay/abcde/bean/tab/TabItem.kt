package cn.martinkay.abcde.bean.tab

import androidx.compose.ui.graphics.vector.ImageVector

data class TabItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val content: String
)
