package com.numq.firebasechat.message

import com.numq.firebasechat.paging.LimitPagingSource

class MessagePagingSource(limit: Int, loadMore: (Int, Int) -> List<Message>) :
    LimitPagingSource<Message>(limit, loadMore)