package com.zj.composerefreshlayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zj.composerefreshlayout.sample.*
import com.zj.composerefreshlayout.ui.theme.ComposeRefreshLayoutTheme
import com.zj.composerefreshlayout.ui.widget.HeaderTitle
import com.zj.composerefreshlayout.ui.widget.RefreshColumnItem
import com.zj.refreshlayout.GlowIndicator
import com.zj.refreshlayout.SwipeRefreshLayout
import com.zj.refreshlayout.SwipeRefreshStyle
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ComposeRefreshLayoutTheme {
                ProvideWindowInsets {
                    rememberSystemUiController().setStatusBarColor(
                        Color.Transparent,
                        darkIcons = true
                    )
                    Surface(color = MaterialTheme.colors.background) {
                        HomeScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    LazyColumn {
        item {
            HeaderTitle(title = "使用示例", false)
        }
        item {
            RefreshColumnItem("Basic", "基本使用") {
                BasicUsageActivity.navigate(context)
            }
        }
        item {
            RefreshColumnItem("Custom", "自定义Header") {
                CustomHeaderActivity.navigate(context)
            }
        }
        item {
            RefreshColumnItem("Custom", "自定义Header使用Lottie") {
                CustomLottieHeaderActivity.navigate(context)
            }
        }
        item {
            RefreshColumnItem("FixedBehind", "下拉的时候Header固定在背后") {
                FixedBehindHeaderActivity.navigate(context)
            }
        }
        item {
            RefreshColumnItem("FixedFront", "下拉的时候Header固定在前面") {
                FixedFrontHeaderActivity.navigate(context)
            }
        }
        item {
            RefreshColumnItem("FixedContent", "下拉的时候内容不动,Header向下滑动") {
                FixedContentHeaderActivity.navigate(context)
            }
        }
    }
}


@Composable
fun RefreshLayoutDemo() {
    val list = (1..20).toList()
    var refreshing by remember { mutableStateOf(false) }
    LaunchedEffect(refreshing) {
        if (refreshing) {
            delay(2000)
            refreshing = false
        }
    }
    SwipeRefreshLayout(
        isRefreshing = refreshing,
        onRefresh = { refreshing = true },
        swipeStyle = SwipeRefreshStyle.FixedFront,
        indicator = {
            GlowIndicator(it)
        }
    ) {
        LazyColumn() {
            items(list) {
                RefreshColumnItem("Basic", "基本使用")
            }
        }
    }
}

@Composable
fun ColumnItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.LightGray)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .background(Color.White)
        )
    }
}