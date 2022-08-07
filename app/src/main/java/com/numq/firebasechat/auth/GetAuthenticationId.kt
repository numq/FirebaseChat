package com.numq.firebasechat.auth

import com.numq.firebasechat.interactor.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAuthenticationId @Inject constructor(
    private val repository: AuthRepository
) : UseCase<Unit, Flow<String?>>() {
    override suspend fun execute(arg: Unit) = repository.authenticationId
}