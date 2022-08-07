package com.numq.firebasechat.chat

import com.numq.firebasechat.message.Message

data class ChatState(
    val chat: Chat? = null,
    val messages: List<Message> = emptyList(),
    val exception: Exception? = null
)