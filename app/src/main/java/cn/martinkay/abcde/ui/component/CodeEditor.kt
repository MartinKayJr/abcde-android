package cn.martinkay.abcde.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import io.github.rosemoe.sora.langs.textmate.TextMateColorScheme
import io.github.rosemoe.sora.langs.textmate.TextMateLanguage
import io.github.rosemoe.sora.langs.textmate.registry.FileProviderRegistry
import io.github.rosemoe.sora.langs.textmate.registry.GrammarRegistry
import io.github.rosemoe.sora.langs.textmate.registry.ThemeRegistry
import io.github.rosemoe.sora.langs.textmate.registry.model.ThemeModel
import io.github.rosemoe.sora.widget.CodeEditor
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme
import org.eclipse.tm4e.core.registry.IThemeSource

@Composable
fun CodeEditor(
    modifier: Modifier = Modifier,
    colorScheme: EditorColorScheme = EditorColorScheme(),
    properties: ((CodeEditor) -> Unit)? = null
) {
    val context = LocalContext.current
    val editor = remember {
        CodeEditor(context)
            .also {
                it.colorScheme = colorScheme
                properties?.invoke(it)
            }
    }
    AndroidView(
        factory = {
            val themes = arrayOf("darcula", "abyss", "quietlight", "solarized_drak")
            val themeRegistry = ThemeRegistry.getInstance()
            themes.forEach { name ->
                val path = "textmate/$name.json"
                themeRegistry.loadTheme(
                    ThemeModel(
                        IThemeSource.fromInputStream(
                            FileProviderRegistry.getInstance().tryGetInputStream(path), path, null
                        ), name
                    ).apply {
                        if (name != "quietlight") {
                            isDark = true
                        }
                    }
                )
            }

            ThemeRegistry.getInstance().setTheme("quietlight")
            GrammarRegistry.getInstance().loadGrammars("textmate/languages.json")
            editor.colorScheme = TextMateColorScheme.create(ThemeRegistry.getInstance())
            val languageScopeName = "source.abc"
            val language = TextMateLanguage.create(
                languageScopeName, true
            )
            editor.setEditorLanguage(language)
            editor
        },
        modifier = modifier.fillMaxSize(),
        onRelease = { it.release() }
    )
}