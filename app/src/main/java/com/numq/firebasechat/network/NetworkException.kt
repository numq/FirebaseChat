package com.numq.firebasechat.network

sealed interface NetworkException {
    object Default : Exception("Unable to connect to internet"), NetworkException
}