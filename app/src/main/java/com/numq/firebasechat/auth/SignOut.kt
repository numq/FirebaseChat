package com.numq.firebasechat.auth

import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class SignOut @Inject constructor(
    private val repository: AuthRepository
) : UseCase<Unit, Unit>() {
    override suspend fun execute(arg: Unit) = repository.signOut()
}