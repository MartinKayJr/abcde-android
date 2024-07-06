package cn.martinkay.abcde.page

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import cn.martinkay.abcde.PermissionActivity
import cn.martinkay.abcde.util.FileSelectorDialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.linpopopo.fileselector.FileSelectorData
import com.linpopopo.fileselector.mediumPadding
import me.yricky.oh.abcd.AbcBuf
import me.yricky.oh.abcd.AbcHeader
import me.yricky.oh.common.wrapAsLEByteBuf
import java.io.File
import java.net.URI
import java.nio.ByteOrder
import java.nio.channels.FileChannel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalPermissionsApi::class)
@Composable
fun WelcomePage(
    context: Context,
    setAppState: (AbcBuf?) -> Unit
) {
    InitPermission(context)
    // 选择路径
    val selectedPaths = remember { mutableStateListOf<String>() }
    // 文件选择器状态
    var fileSelectorState by remember { mutableStateOf(false) }
    // 文件选择器数据
    var fileSelectorData by remember { mutableStateOf(FileSelectorData(rootPath = Environment.getExternalStorageDirectory().canonicalPath)) }

    Box(Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text("ABCDecoder", style = MaterialTheme.typography.displayLarge)
            TextButton(onClick = {
                fileSelectorData = fileSelectorData.copy(
                    isSelectFile = true,
                    isMultiple = false,
                )
                fileSelectorState = true
            }) {
                Text("选择文件")
            }
            Spacer(modifier = Modifier.height(mediumPadding))
            LazyColumn {
                itemsIndexed(selectedPaths) { _, item ->
                    Text(item)
                }
            }
            if (fileSelectorState) {
                FileSelectorDialog(
                    fileSelectorData = fileSelectorData,
                    onDismissRequest = { fileSelectorState = false },
                    onClose = { fileSelectorState = false },
                    onSelectedPaths = { paths ->
                        fileSelectorState = false
                        selectedPaths.clear()
                        selectedPaths.addAll(paths)
                        setAppState(
                            selectedPaths.firstNotNullOfOrNull { path ->
                                File(URI("file://$path")).takeIf {
                                    it.isFile && it.extension.uppercase() == "ABC" && it.length() > AbcHeader.SIZE
                                }
                            }?.let { abcFile ->
                                AbcBuf(abcFile.path,
                                    FileChannel.open(abcFile.toPath())
                                        .map(FileChannel.MapMode.READ_ONLY, 0, abcFile.length())
                                        .let { wrapAsLEByteBuf(it.order(ByteOrder.LITTLE_ENDIAN)) }
                                ).takeIf { it.header.isValid() }
                            }
                        )
                    }
                )
            }

        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun InitPermission(context: Context) {
    val multiplePermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )
    if (!multiplePermissionsState.allPermissionsGranted) {
        // 跳转到PermissionActivity手动开启
        val intent = Intent(context, PermissionActivity::class.java)
        context.startActivity(intent)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if (!Environment.isExternalStorageManager()) {
            // 跳转到PermissionActivity手动开启
            val intent = Intent(context, PermissionActivity::class.java)
            context.startActivity(intent)
        }
    }
}
