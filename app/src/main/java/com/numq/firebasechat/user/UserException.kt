package com.numq.firebasechat.user

sealed interface UserException {
    object Default : UserException, Exception("Failed to connect to user service")
}