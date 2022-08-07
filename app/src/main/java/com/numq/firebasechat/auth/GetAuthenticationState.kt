package com.numq.firebasechat.auth

import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class GetAuthenticationState @Inject constructor(
    private val repository: AuthRepository
) : UseCase<Unit, Boolean>() {
    override suspend fun execute(arg: Unit) = repository.authenticationState
}