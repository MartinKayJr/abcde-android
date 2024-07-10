package cn.martinkay.abcde.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.martinkay.abcde.BuildConfig
import cn.martinkay.abcde.R
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavController
) {
    ScreenContainer(
        navController,
        title = {
            Text("Help")
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 35.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                color = Color.Black,
                fontSize = 34.sp,
            )
            HorizontalDivider(
                thickness = 4.dp,
                modifier = Modifier
                    .width(220.dp)
                    .padding(top = 10.dp, bottom = 10.dp)
            )
            Spacer(modifier = Modifier
                .width(0.dp)
                .height(15.dp))
            Text(
                text = stringResource(id = R.string.about_email),
                color = Color(0xFF999999),
                fontSize = 14.sp,
            )
            Text(
                text = stringResource(id = R.string.nav_header_subtitle),
                color = Color(0xFF263238),
                fontSize = 18.sp,
            )
            Spacer(modifier = Modifier
                .width(0.dp)
                .height(15.dp))
            Text(
                text = stringResource(id = R.string.version),
                color = Color(0xFF999999),
                fontSize = 14.sp,
            )
            Text(
                text = String.format(
                    "%s(Build Time:%s)",
                    BuildConfig.VERSION_NAME,
                    // 时间戳转换为时间BUILD_TIMESTAMP
                    timestampToDateTime(BuildConfig.BUILD_TIMESTAMP)
                ),
                color = Color(0xFF263238),
                fontSize = 18.sp,
            )
            Spacer(modifier = Modifier
                .width(0.dp)
                .height(15.dp))
            Text(
                text = stringResource(id = R.string.website),
                color = Color(0xFF999999),
                fontSize = 14.sp,
            )
            Text(
                text = stringResource(id = R.string.website_rul),
                color = Color(0xFF263238),
                fontSize = 18.sp,
            )
            Spacer(modifier = Modifier
                .width(0.dp)
                .height(15.dp))
        }
    }
}

fun timestampToDateTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = Date(timestamp)
    return sdf.format(date)
}
