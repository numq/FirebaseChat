package com.numq.firebasechat.home

import com.numq.firebasechat.chat.Chat
import com.numq.firebasechat.user.User

data class HomeState(
    val currentUser: User? = null,
    val activeChat: Chat? = null,
    val exception: Exception? = null
)