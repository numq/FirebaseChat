package com.numq.firebasechat.user

import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class DeleteUser @Inject constructor(
    private val repository: UserRepository
) : UseCase<String, String>() {
    override suspend fun execute(arg: String) = repository.deleteUser(arg)
}