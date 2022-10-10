package com.numq.firebasechat.search

sealed interface SearchException {
    object Default : SearchException, Exception("Failed to connect to search service")
}