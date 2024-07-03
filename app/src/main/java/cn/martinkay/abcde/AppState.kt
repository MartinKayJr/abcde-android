package cn.martinkay.abcde

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*

class AppState {
    val pageStack = mutableStateListOf<Page>()
    var currPage:Page? by mutableStateOf(null)



    sealed class Page {
        abstract val tag: String
    }


}