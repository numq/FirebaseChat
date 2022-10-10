package com.numq.firebasechat.search

import com.numq.firebasechat.interactor.UseCase
import com.numq.firebasechat.user.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindUsersByQuery @Inject constructor(
    private val repository: SearchRepository
) : UseCase<String, Flow<User>>() {
    override suspend fun execute(arg: String) = repository.findUsersByQuery(arg)
}