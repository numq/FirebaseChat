package com.numq.firebasechat.user

import com.numq.firebasechat.interactor.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserById @Inject constructor(
    private val repository: UserRepository
) : UseCase<String, Flow<User>>() {
    override suspend fun execute(arg: String) = repository.getUserById(arg)
}