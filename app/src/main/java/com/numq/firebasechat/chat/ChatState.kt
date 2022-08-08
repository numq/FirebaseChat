package com.numq.firebasechat.chat

import com.numq.firebasechat.message.Message

data class ChatState(
    val messages: List<Message> = listOf(),
    val exception: Exception? = null
)