package com.zj.composerefreshlayout.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zj.composerefreshlayout.customheader.LottieHeaderTwo
import com.zj.composerefreshlayout.ui.theme.ComposeRefreshLayoutTheme
import com.zj.composerefreshlayout.ui.widget.HeaderTitle
import com.zj.composerefreshlayout.ui.widget.RefreshColumnItem
import com.zj.composerefreshlayout.utils.getActivity
import com.zj.refreshlayout.SwipeRefreshLayout
import com.zj.refreshlayout.SwipeRefreshStyle
import kotlinx.coroutines.delay

class FixedBehindHeaderActivity : ComponentActivity() {
    companion object {
        fun navigate(context: Context) {
            context.startActivity(Intent(context, FixedBehindHeaderActivity::class.java))
        }
    }

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
                        FixedBehindHeaderSample()
                    }
                }
            }
        }
    }
}

@Composable
fun FixedBehindHeaderSample() {
    val activity = LocalContext.current.getActivity()
    var refreshing by remember { mutableStateOf(false) }
    val list = (1..20).toList()
    LaunchedEffect(refreshing) {
        if (refreshing) {
            delay(3000)
            refreshing = false
        }
    }
    Column() {
        HeaderTitle(title = "FixedBehind Header", true) {
            activity?.finish()
        }
        SwipeRefreshLayout(
            isRefreshing = refreshing,
            onRefresh = { refreshing = true },
            swipeStyle = SwipeRefreshStyle.FixedBehind,
            indicator = {
                LottieHeaderTwo(state = it)
            }
        ) {
            LazyColumn(modifier = Modifier.background(Color.White)) {
                items(list) {
                    val title = "第${it}条数据"
                    val subTitle = "这是测试的第${it}条数据"
                    RefreshColumnItem(title = title, subTitle = subTitle)
                }
            }
        }
    }
}