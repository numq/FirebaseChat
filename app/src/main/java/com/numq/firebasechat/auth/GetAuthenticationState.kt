package com.numq.firebasechat.auth

import com.numq.firebasechat.interactor.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAuthenticationState @Inject constructor(
    private val repository: AuthRepository
) : UseCase<Unit, Flow<AuthenticationState>>() {
    override suspend fun execute(arg: Unit) = repository.getAuthenticationState()
}