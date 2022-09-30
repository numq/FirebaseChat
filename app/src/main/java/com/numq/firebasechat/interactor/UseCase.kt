package com.numq.firebasechat.interactor

import android.util.Log
import arrow.core.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class UseCase<in T, out R> {

    private val coroutineContext = Dispatchers.Default + Job()
    private val coroutineScope = CoroutineScope(coroutineContext)

    abstract suspend fun execute(arg: T): Either<Exception, R>

    operator fun invoke(arg: T, onException: (Exception) -> Unit, onResult: (R) -> Unit = {}) {
        coroutineScope.launch {
            execute(arg).tap {
                Log.d(this@UseCase.javaClass.simpleName, it.toString())
            }.tapLeft {
                Log.e(
                    this@UseCase.javaClass.simpleName,
                    it.localizedMessage ?: it::class.java.simpleName
                )
            }.fold(onException, onResult)
        }
    }
}