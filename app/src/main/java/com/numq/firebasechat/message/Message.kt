package com.numq.firebasechat.message

data class Message(
    val id: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val text: String = "",
    val delivered: Boolean = false,
    val read: Boolean = false,
    val sentAt: Long = System.currentTimeMillis(),
    val updatedAt: Long? = null
)
