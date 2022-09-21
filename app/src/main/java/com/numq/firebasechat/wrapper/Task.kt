package com.numq.firebasechat.wrapper

import arrow.core.Either
import arrow.core.flatten
import arrow.core.left
import arrow.core.right
import com.google.android.gms.tasks.Task
import com.numq.firebasechat.network.NetworkApi
import com.numq.firebasechat.network.NetworkException
import kotlinx.coroutines.tasks.await

suspend fun <T> Task<T>.wrap() = runCatching { await() }.fold({ it.right() },
    { Exception(it.localizedMessage).left() })

suspend fun <T> Task<T>.wrap(networkApi: NetworkApi?) = networkApi?.let {
    Either.conditionally(
        networkApi.isAvailable,
        ifFalse = { NetworkException.Default },
        ifTrue = { wrap() }).flatten()
} ?: wrap()