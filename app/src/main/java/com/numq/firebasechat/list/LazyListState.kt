package com.numq.firebasechat.list

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*

@Composable
fun LazyListState.isReachedTheEnd(
    buffer: Int = 0,
    onReachedTheEnd: () -> Unit
) {
    val shouldLoadMore by remember {
        derivedStateOf { layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1 - buffer }
    }
    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore }.collect { if (it) onReachedTheEnd() }
    }
}