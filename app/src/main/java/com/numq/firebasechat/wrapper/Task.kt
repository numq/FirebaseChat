package com.numq.firebasechat.wrapper

import arrow.core.Either
import arrow.core.flatten
import arrow.core.left
import arrow.core.right
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.tasks.await

suspend fun <T> Task<T>.wrap() = runCatching { await() }.fold({ it.right() },
    { Exception(it.localizedMessage).left() })

suspend fun <T> Task<T>.wrapIf(condition: Boolean, exception: Exception) =
    Either.conditionally(
        condition,
        ifFalse = { exception },
        ifTrue = { wrap() }).flatten()