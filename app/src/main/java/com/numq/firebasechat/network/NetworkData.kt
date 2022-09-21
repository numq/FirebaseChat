package com.numq.firebasechat.network

import arrow.core.right
import javax.inject.Inject

class NetworkData @Inject constructor(
    service: NetworkApi
) : NetworkRepository {
    override val state = service.state.right()
    override val isAvailable = service.isAvailable.right()
}