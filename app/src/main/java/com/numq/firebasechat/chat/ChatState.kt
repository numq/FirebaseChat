package com.numq.firebasechat.chat

import com.numq.firebasechat.message.Message
import com.numq.firebasechat.user.User

data class ChatState(
    val user: User? = null,
    val chat: Chat? = null,
    val messages: List<Message> = emptyList(),
    val exception: Exception? = null
)