package com.numq.firebasechat.settings

import com.numq.firebasechat.user.User

data class SettingsState(
    val currentUser: User? = null,
    val exception: Exception? = null
)