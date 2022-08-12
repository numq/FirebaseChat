package com.numq.firebasechat.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

abstract class LimitPagingSource<T : Any> constructor(
    private val limit: Int,
    private val loadMore: (Int, Int) -> List<T>
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? = null

    override suspend fun load(params: LoadParams<Int>) = try {
        val page = params.key ?: 0
        val data = loadMore(page * limit, limit)
        LoadResult.Page(
            data = data,
            prevKey = if (page > 0) page - 1 else null,
            nextKey = if (data.isEmpty()) null else page + 1
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }
}