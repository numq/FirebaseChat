package com.numq.firebasechat.user

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val imageUri: String? = null,
    val isOnline: Boolean = false,
    val lastSeenAt: Long = System.currentTimeMillis()
)