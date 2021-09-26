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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.zj.composerefreshlayout.ui.theme.ComposeRefreshLayoutTheme

class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeRefreshLayoutTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    SwipeableDemo()
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun SwipeableDemo() {
    val offsetPx = remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (source == NestedScrollSource.Drag && available.y > 0) {
                    offsetPx.value = (offsetPx.value + available.y).coerceAtMost(150f)
                    return Offset(0f, available.y)
                }
                return super.onPostScroll(consumed, available, source)
            }
        }
    }
    val list = (1..20).toList()
    Box(
        modifier = Modifier
            .offset {
                IntOffset(0, offsetPx.value.toInt())
            }
            .nestedScroll(nestedScrollConnection)
            .fillMaxSize()
    ) {
        Text(
            text = "正在刷新",
            color = Color.White,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .offset {
                    IntOffset(0, -150)
                }
                .fillMaxWidth()
                .clipToBounds()
                .height(50.dp)
                .background(Color.Blue)
        )
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