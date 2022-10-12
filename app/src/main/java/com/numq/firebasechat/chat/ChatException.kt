package com.numq.firebasechat.chat

sealed interface ChatException {
    object Default : ChatException, Exception("Failed to connect to chat service")
}