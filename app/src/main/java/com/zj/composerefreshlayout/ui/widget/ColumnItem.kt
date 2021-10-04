package com.zj.composerefreshlayout.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RefreshColumnItem(title: String, subTitle: String, callback: (() -> Unit) = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp, 0.dp)
            .clickable {
                callback.invoke()
            }
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
        Text(text = title, color = Color.Black, style = MaterialTheme.typography.body1)
        Text(text = subTitle, color = Color(0xff888888), style = MaterialTheme.typography.caption)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5f.dp)
                .background(Color(0xffe9e9e9))
        )
    }
}