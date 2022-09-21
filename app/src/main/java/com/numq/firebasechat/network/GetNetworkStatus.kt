package com.numq.firebasechat.network

import com.numq.firebasechat.interactor.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNetworkStatus @Inject constructor(
    private val repository: NetworkRepository
) : UseCase<Unit, Flow<NetworkStatus>>() {
    override suspend fun execute(arg: Unit) = repository.state
}