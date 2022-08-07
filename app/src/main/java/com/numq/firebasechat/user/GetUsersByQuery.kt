package com.numq.firebasechat.user

import arrow.core.Either
import com.numq.firebasechat.interactor.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersByQuery @Inject constructor(
    private val repository: UserRepository
) : UseCase<Pair<String, Long>, Flow<User>>() {
    override suspend fun execute(arg: Pair<String, Long>): Either<Exception, Flow<User>> {
        val (query, limit) = arg
        return repository.getUsersByQuery(query, limit)
    }
}