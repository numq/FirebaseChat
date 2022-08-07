package com.numq.firebasechat.user

import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class UpdateLastActiveChatId @Inject constructor(
    private val repository: UserRepository
) : UseCase<User, User>() {
    override suspend fun execute(arg: User) = repository.updateUser(arg)
}