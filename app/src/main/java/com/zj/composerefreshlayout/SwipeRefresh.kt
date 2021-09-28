/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zj.composerefreshlayout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset

/**
 * A layout which implements the swipe-to-refresh pattern, allowing the user to refresh content via
 * a vertical swipe gesture.
 *
 * This layout requires its content to be scrollable so that it receives vertical swipe events.
 * The scrollable content does not need to be a direct descendant though. Layouts such as
 * [androidx.compose.foundation.lazy.LazyColumn] are automatically scrollable, but others such as
 * [androidx.compose.foundation.layout.Column] require you to provide the
 * [androidx.compose.foundation.verticalScroll] modifier to that content.
 *
 * Apps should provide a [onRefresh] block to be notified each time a swipe to refresh gesture
 * is completed. That block is responsible for updating the [state] as appropriately,
 * typically by setting [SwipeRefreshState.isRefreshing] to `true` once a 'refresh' has been
 * started. Once a refresh has completed, the app should then set
 * [SwipeRefreshState.isRefreshing] to `false`.
 *
 * If an app wishes to show the progress animation outside of a swipe gesture, it can
 * set [SwipeRefreshState.isRefreshing] as required.
 *
 * This layout does not clip any of it's contents, including the indicator. If clipping
 * is required, apps can provide the [androidx.compose.ui.draw.clipToBounds] modifier.
 *
 * @sample com.google.accompanist.sample.swiperefresh.SwipeRefreshSample
 *
 * @param state the state object to be used to control or observe the [SwipeRefresh] state.
 * @param onRefresh Lambda which is invoked when a swipe to refresh gesture is completed.
 * @param modifier the modifier to apply to this layout.
 * @param swipeEnabled Whether the the layout should react to swipe gestures or not.
 * @param refreshTriggerDistance The minimum swipe distance which would trigger a refresh.
 * @param indicatorAlignment The alignment of the indicator. Defaults to [Alignment.TopCenter].
 * @param indicatorPadding Content padding for the indicator, to inset the indicator in if required.
 * @param indicator the indicator that represents the current state. By default this
 * will use a [SwipeRefreshIndicator].
 * @param clipIndicatorToPadding Whether to clip the indicator to [indicatorPadding]. If false is
 * provided the indicator will be clipped to the [content] bounds. Defaults to true.
 * @param content The content containing a scroll composable.
 */
@Composable
fun SwipeRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    swipeEnabled: Boolean = true,
    refreshTriggerRate: Float = 1f, //刷新生效高度与refreshHeader的比例
    indicator: @Composable (state: SwipeRefreshState) -> Unit = {
        ClassicRefreshHeader(it)
    },
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val updatedOnRefresh = rememberUpdatedState(onRefresh)
    var indicatorHeight by remember {
        mutableStateOf(0)
    }
    val refreshTrigger = indicatorHeight * refreshTriggerRate
    val state = rememberSwipeRefreshState(isRefreshing, refreshTrigger)
    LaunchedEffect(state.isSwipeInProgress, state.isRefreshing) {
        // If there's no swipe currently in progress, animate to the correct resting position
        if (!state.isSwipeInProgress) {
            if (state.isRefreshing) {
                state.animateOffsetTo(refreshTrigger)
            } else {
                state.animateOffsetTo(0f)
            }
        }
    }

    // Our nested scroll connection, which updates our state.
    val nestedScrollConnection = remember(state, coroutineScope) {
        SwipeRefreshNestedScrollConnection(state, coroutineScope) {
            // On refresh, re-dispatch to the update onRefresh block
            updatedOnRefresh.value.invoke()
        }
    }.apply {
        this.enabled = swipeEnabled
        this.refreshTrigger = refreshTrigger
    }

    Box(
        modifier
            .nestedScroll(connection = nestedScrollConnection)
    ) {
        Box(modifier = Modifier
            .onGloballyPositioned {
                indicatorHeight = it.size.height
            }
            .offset {
                IntOffset(0, state.indicatorOffset.toInt() - indicatorHeight)
            }) {
            ClassicRefreshHeader(state)
        }
        Box(modifier = Modifier.offset {
            IntOffset(0, state.indicatorOffset.toInt())
        }) {
            content()
        }
    }
}
