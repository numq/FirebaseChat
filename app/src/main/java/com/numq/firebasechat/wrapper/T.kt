package com.numq.firebasechat.wrapper

import arrow.core.Either
import arrow.core.flatten
import arrow.core.left
import arrow.core.right

fun <T> T.wrap(): Either<Exception, T> = runCatching { this }.fold({ it.right() },
    { Exception(it.localizedMessage).left() })

fun <T> T.wrapIf(condition: Boolean, exception: Exception): Either<Exception, T> =
    Either.conditionally(
        condition,
        ifFalse = { exception },
        ifTrue = { wrap() }).flatten()