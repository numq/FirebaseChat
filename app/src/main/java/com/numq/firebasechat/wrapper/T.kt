package com.numq.firebasechat.wrapper

import arrow.core.Either
import arrow.core.flatten
import arrow.core.left
import arrow.core.right
import com.numq.firebasechat.network.NetworkApi
import com.numq.firebasechat.network.NetworkException

fun <T> T.wrap(): Either<Exception, T> = runCatching { this }.fold({ it.right() },
        { Exception(it.localizedMessage).left() })

fun <T> T.wrap(networkApi: NetworkApi? = null): Either<Exception, T> = networkApi?.let {
    Either.conditionally(
        networkApi.isAvailable,
        ifFalse = { NetworkException.Default },
        ifTrue = { wrap() }).flatten()
} ?: wrap()