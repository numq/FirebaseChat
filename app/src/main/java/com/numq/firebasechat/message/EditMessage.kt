package com.numq.firebasechat.message

import arrow.core.Either
import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class EditMessage @Inject constructor(
    private val repository: MessageRepository
) : UseCase<Pair<String, String>, Boolean>() {
    override suspend fun execute(arg: Pair<String, String>): Either<Exception, Boolean> {
        val (id, text) = arg
        return repository.updateMessage(id, text)
    }
}