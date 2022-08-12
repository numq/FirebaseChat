package com.numq.firebasechat.chat

import com.numq.firebasechat.paging.LimitPagingSource

class ChatPagingSource(limit: Int, loadMore: (Int, Int) -> List<Chat>) :
    LimitPagingSource<Chat>(limit, loadMore)