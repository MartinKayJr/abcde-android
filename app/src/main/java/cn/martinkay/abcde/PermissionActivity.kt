package cn.martinkay.abcde

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import cn.martinkay.abcde.ui.theme.AbcdeTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState

class PermissionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AbcdeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PermissionPage(
                        LocalContext.current,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionPage(
    context: Context,
    modifier: Modifier = Modifier
) {
    val multiplePermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )
    Column(modifier = Modifier.padding(10.dp)) {
        Text(
            getTextToShowGivenPermissions(
                multiplePermissionsState.revokedPermissions, // 被拒绝/撤销的权限列表
                multiplePermissionsState.shouldShowRationale
            ),
            fontSize = 16.sp
        )
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            multiplePermissionsState.launchMultiplePermissionRequest()
            if (!Environment.isExternalStorageManager()) {
                //调转到申请权限页面手动开启
                val intent =
                    Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                context.packageName
                intent.data = Uri.parse("package:${context.packageName}")
                ActivityCompat.startActivity(context, intent, null)
            }
        }) {
            Text("请求权限")
        }
        multiplePermissionsState.permissions.forEach {
            HorizontalDivider()
            Text(
                text = "权限名：${it.permission} \n " +
                        "授权状态：${it.status.isGranted} \n ",
                fontSize = 16.sp
            )
        }
        HorizontalDivider()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun getTextToShowGivenPermissions(
    permissions: List<PermissionState>,
    shouldShowRationale: Boolean
): String {
    val size = permissions.size
    if (size == 0) return ""
    val textToShow = StringBuilder().apply { append("以下权限：") }
    for (i in permissions.indices) {
        textToShow.append(permissions[i].permission).apply {
            if (i == size - 1) append(" ") else append(", ")
        }
    }
    textToShow.append(
        if (shouldShowRationale) {
            " 需要被授权，以保证应用功能正常使用."
        } else {
            " 被拒绝使用. 应用功能将不能正常使用."
        }
    )
    return textToShow.toString()
}

@Preview(showBackground = true)
@Composable
fun PermissionPreview() {
    AbcdeTheme {
        PermissionPage(LocalContext.current)
    }
}