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
import androidx.compose.ui.unit.dp
import com.zj.composerefreshlayout.customheader.LottieHeaderTwo
import com.zj.composerefreshlayout.ui.theme.ComposeRefreshLayoutTheme
import com.zj.refreshlayout.SwipeRefreshLayout
import com.zj.refreshlayout.SwipeRefreshStyle
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeRefreshLayoutTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    RefreshLayoutDemo()
                }
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
        swipeStyle = SwipeRefreshStyle.FixedContent,
        indicator = {
            LottieHeaderTwo(it)
        }
    ) {
        LazyColumn() {
            items(list) {
                ColumnItem()
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