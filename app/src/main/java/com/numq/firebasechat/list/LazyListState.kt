package com.numq.firebasechat.list

import androidx.compose.foundation.lazy.LazyListState

val LazyListState.isReachedTheEnd: Boolean
    get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1