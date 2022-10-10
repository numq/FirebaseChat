package com.numq.firebasechat.search

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.leftIfNull
import com.numq.firebasechat.network.NetworkApi
import com.numq.firebasechat.network.NetworkException
import com.numq.firebasechat.user.User
import com.numq.firebasechat.wrapper.wrapIf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import javax.inject.Inject

interface SearchRepository {

    fun findUsersByQuery(query: String): Either<Exception, Flow<User>>

    class Implementation @Inject constructor(
        private val networkService: NetworkApi,
        private val searchService: SearchApi
    ) : SearchRepository {

        companion object {
            const val DEFAULT_LIMIT = 10L
        }

        override fun findUsersByQuery(query: String) =
            searchService.getUsersByName(query, DEFAULT_LIMIT)
                .wrapIf(networkService.isAvailable, NetworkException.Default)
                .leftIfNull { SearchException.Default }
                .flatMap {
                    searchService.getUsersByEmail(query, DEFAULT_LIMIT)
                        .wrapIf(networkService.isAvailable, NetworkException.Default)
                }.map {
                    it.distinctUntilChangedBy { user -> user.id }
                }

    }

}