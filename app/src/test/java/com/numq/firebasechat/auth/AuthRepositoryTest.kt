package com.numq.firebasechat.auth

import arrow.core.right
import com.google.android.gms.tasks.Tasks
import com.numq.firebasechat.network.NetworkApi
import com.numq.firebasechat.user.User
import com.numq.firebasechat.user.UserApi
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AuthRepositoryTest {

    private lateinit var repository: AuthRepository

    @MockK
    private lateinit var networkService: NetworkApi

    @MockK
    private lateinit var authService: AuthApi

    @MockK
    private lateinit var userService: UserApi

    @Before
    fun before() {
        MockKAnnotations.init(this)
        every { networkService.isAvailable } returns true
        repository = AuthRepository.Implementation(networkService, authService, userService)
    }

    @Test
    fun `should return authentication state`() = runBlocking {
        val id = "test"
        val state = flowOf(AuthenticationState.Authenticated(id))
        every { authService.getAuthenticationState() } returns state
        assertEquals(state.right(), repository.getAuthenticationState())
    }

    @Test
    fun `should sign in by email and return user`() = runBlocking {
        every { authService.signInByEmail(any(), any()) } returns true
        assertEquals(true.right(), repository.signInByEmail("test", "test"))
    }

    @Test
    fun `should sign up by email and return user`() = runBlocking {
        every { authService.signUpByEmail(any(), any(), any(), any()) } returns true
        every { userService.createUser(any(), any(), any()) } returns Tasks.forResult(User())
        assertEquals(true.right(), repository.signUpByEmail("test", "test", "test"))
    }

    @Test
    fun `should sign out`() = runBlocking {
        every { authService.signOut() } returns true
        assertEquals(true.right(), repository.signOut())
    }
}