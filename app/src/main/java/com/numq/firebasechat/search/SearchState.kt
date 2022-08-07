package com.numq.firebasechat.search

import com.numq.firebasechat.user.User

data class SearchState(
    val searchResults: List<User> = emptyList(),
    val exception: Exception? = null
)