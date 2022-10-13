package com.numq.firebasechat.message

sealed interface MessageException {
    object Default : MessageException, Exception("Failed to connect to message service")
}