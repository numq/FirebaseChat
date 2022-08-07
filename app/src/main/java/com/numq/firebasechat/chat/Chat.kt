package com.numq.firebasechat.chat

import com.numq.firebasechat.message.Message

data class Chat(
    val id: String = "",
    val name: String = "",
    val userIds: List<String> = emptyList(),
    val typingUserIds: List<String>? = null,
    val lastMessage: Message? = null,
    val updatedAt: Long = 0L
)
