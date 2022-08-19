package com.numq.firebasechat.user

import arrow.core.Either
import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class UploadImage @Inject constructor(
    private val repository: UserRepository
) : UseCase<Pair<String, ByteArray>, User>() {
    override suspend fun execute(arg: Pair<String, ByteArray>): Either<Exception, User> {
        val (id, bytes) = arg
        return repository.uploadImage(id, bytes)
    }
}