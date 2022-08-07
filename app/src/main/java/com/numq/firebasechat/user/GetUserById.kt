package com.numq.firebasechat.user

import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class GetUserById @Inject constructor(
    private val repository: UserRepository
) : UseCase<String, User>() {
    override suspend fun execute(arg: String) = repository.getUserById(arg)
}