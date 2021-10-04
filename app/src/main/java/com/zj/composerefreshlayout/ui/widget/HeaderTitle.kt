package com.zj.composerefreshlayout.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsHeight
import com.zj.composerefreshlayout.R

@Composable
fun HeaderTitle(title: String, showArrow: Boolean = true, callback: (() -> Unit) = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsHeight()
                .background(Color(0xff2299ee))
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .background(Color(0xff33aaff))
                .padding(16.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showArrow) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_back_white_24dp),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 16.dp, 0.dp)
                        .size(24.dp)
                        .clickable {
                            callback.invoke()
                        }
                )
            }
            Text(text = title, color = Color.White, style = MaterialTheme.typography.h6)
        }
    }
}