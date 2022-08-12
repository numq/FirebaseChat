package com.numq.firebasechat.chat

import androidx.paging.PagingData
import com.numq.firebasechat.message.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ChatState(
    val messages: Flow<PagingData<Message>> = emptyFlow(),
    val exception: Exception? = null
)