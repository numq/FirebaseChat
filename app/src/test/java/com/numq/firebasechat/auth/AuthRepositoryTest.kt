package com.numq.firebasechat.auth

import arrow.core.right
import com.google.android.gms.tasks.Tasks
import com.numq.firebasechat.user.User
import com.numq.firebasechat.user.UserService
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
    private lateinit var authService: AuthService

    @MockK
    private lateinit var userService: UserService

    @Before
    fun before() {
        MockKAnnotations.init(this)
        repository = AuthData(authService, userService)
    }

    @Test
    fun `should return authentication state`() = runBlocking {
        val id = "test"
        val input = flowOf(AuthenticationState.Authenticated(id))
        every { authService.getAuthenticationState() } returns input
        assertEquals(input.right(), repository.getAuthenticationState())
    }

    @Test
    fun `should sign in by email and return user`() = runBlocking {
        every { authService.signInByEmail(any(), any()) } returns true
        assertEquals(true.right(), repository.signInByEmail("test", "test"))
    }

    @Test
    fun `should sign up by email and return user`() = runBlocking {
        every { authService.signUpByEmail(any(), any(), any()) } returns true
        every { userService.createUser(any(), any(), any()) } returns Tasks.forResult(User())
        assertEquals(true.right(), repository.signUpByEmail("test", "test", "test"))
    }

    @Test
    fun `should sign out`() = runBlocking {
        every { authService.signOut() } returns true
        assertEquals(true.right(), repository.signOut())
    }
}