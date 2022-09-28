package com.numq.firebasechat.network

import arrow.core.Either
import arrow.core.right
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface NetworkRepository {

    val state: Either<Exception, Flow<NetworkStatus>>
    val isAvailable: Either<Exception, Boolean>

    class Implementation @Inject constructor(
        service: NetworkApi
    ) : NetworkRepository {
        override val state = service.state.right()
        override val isAvailable = service.isAvailable.right()
    }

}