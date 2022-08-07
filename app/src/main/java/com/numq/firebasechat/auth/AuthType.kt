package com.numq.firebasechat.auth

sealed class AuthType private constructor() {
    object EmailPassword : AuthType()
}