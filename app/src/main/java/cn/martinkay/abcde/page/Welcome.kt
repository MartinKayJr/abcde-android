package cn.martinkay.abcde.page

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import cn.martinkay.abcde.PermissionActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import me.yricky.oh.abcd.AbcBuf

@OptIn(ExperimentalComposeUiApi::class, ExperimentalPermissionsApi::class)
@Composable
fun WelcomePage(
    context: Context,
    setAppState: (AbcBuf?) -> Unit
) {
    InitPermission(context)
    Box(Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text("ABCDecoder", style = MaterialTheme.typography.displayLarge)

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
