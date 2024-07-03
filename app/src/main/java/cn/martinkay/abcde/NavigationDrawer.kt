package cn.martinkay.abcde

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.martinkay.abcde.bean.menu.MenuGroup
import cn.martinkay.abcde.bean.menu.MenuItem

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 48.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Icon(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_launcher_fg_vector),
                contentDescription = "",
            )
            Text(text = "Abcde", fontSize = 32.sp)
        }
    }
}

@Composable
fun DrawerBody(
    groups: List<MenuGroup>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (MenuItem) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(12.dp)
    ) {
        items(groups) { group ->
            Text(
                text = group.name,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(16.dp)
            )
            DrawerItem(
                modifier = modifier,
                items = group.items,
                itemTextStyle = itemTextStyle,
                onItemClick = onItemClick
            )

        }
    }
}

@Composable
fun DrawerItem(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle,
    onItemClick: (MenuItem) -> Unit
) {
    // 计算高度
    val height = items.size * (48 + 18 + 12)
    LazyColumn(
        modifier = modifier
            .height(height.dp)
            .padding(12.dp)
    ) {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(48.dp))
                    .height(48.dp)
                    .clickable { onItemClick(item) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = item.title,
                        style = itemTextStyle,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

        }
    }
}