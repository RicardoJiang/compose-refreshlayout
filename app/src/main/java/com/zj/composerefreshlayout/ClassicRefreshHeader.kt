package com.zj.composerefreshlayout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ClassicRefreshHeader(state: SwipeRefreshState) {
    val text = when (state.headerState) {
        RefreshHeaderState.PullDownToRefresh -> {
            "下拉刷新"
        }
        RefreshHeaderState.Refreshing -> {
            "正在刷新"
        }
        else -> {
            "释放刷新"
        }
    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(80.dp), contentAlignment = Alignment.Center) {
        Text(
            text = text,
            color = Color.Black,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .fillMaxWidth()
                .clipToBounds()
        )
    }
}