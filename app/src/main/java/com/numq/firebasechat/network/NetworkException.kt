package com.numq.firebasechat.network

sealed interface NetworkException {
    object Default : NetworkException, Exception("Unable to connect to internet")
}