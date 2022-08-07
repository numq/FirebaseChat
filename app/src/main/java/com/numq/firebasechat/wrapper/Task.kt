package com.numq.firebasechat.wrapper

import arrow.core.Either
import arrow.core.left
import arrow.core.rightIfNotNull
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.tasks.await

suspend fun <T> Task<T>.wrap(): Either<Exception, T> = try {
    await().rightIfNotNull { exception ?: Exception("Something get wrong") }
} catch (e: Exception) {
    e.left()
}
