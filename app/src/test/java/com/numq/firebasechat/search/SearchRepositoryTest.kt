package com.numq.firebasechat.search

import arrow.core.right
import com.numq.firebasechat.network.NetworkApi
import com.numq.firebasechat.user.User
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class SearchRepositoryTest {

    private lateinit var repository: SearchRepository

    @MockK
    private lateinit var networkService: NetworkApi

    @MockK
    private lateinit var searchService: SearchApi

    @Before
    fun before() {
        MockKAnnotations.init(this)
        every { networkService.isAvailable } returns true
        repository = SearchRepository.Implementation(networkService, searchService)
    }

    @Test
    fun `should return users by query`() = runBlocking {
        val query = "test"
        val users = arrayOfNulls<User>(10)
            .mapIndexed { idx, _ -> User(id = "$idx") }
        every { searchService.getUsersByName(any(), any()) } returns users.asFlow()
        every { searchService.getUsersByEmail(any(), any()) } returns users.asFlow()

        val output = repository.findUsersByQuery(query).map { it.toList() }
        assertEquals(users.right(), output)
    }

}