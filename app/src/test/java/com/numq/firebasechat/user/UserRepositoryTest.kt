package com.numq.firebasechat.user

import arrow.core.right
import com.google.android.gms.tasks.Tasks
import com.numq.firebasechat.network.NetworkApi
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class UserRepositoryTest {

    private lateinit var repository: UserRepository

    @MockK
    private lateinit var networkService: NetworkApi

    @MockK
    private lateinit var userService: UserApi

    @Before
    fun before() {
        MockKAnnotations.init(this)
        every { networkService.isAvailable } returns true
        repository = UserRepository.Implementation(networkService, userService)
    }

    @Test
    fun `should return flow that contains single user`() = runBlocking {
        val id = "test"
        val users = flowOf(User(id))
        every { userService.getUserById(any()) } returns users

        val output = repository.getUserById(id)
        assertEquals(users.right(), output)
    }

    @Test
    fun `should upload image of user and return updated user`() = runBlocking {
        val (id, bytes, uri) = Triple(
            "test",
            byteArrayOf(Byte.MIN_VALUE, Byte.MAX_VALUE),
            "test_uri"
        )
        val user = User(id, imageUri = uri)
        every { userService.uploadImage(any(), any()) } returns Tasks.forResult(user)

        val output = repository.uploadImage(id, bytes)
        assertEquals(user.right(), output)
    }

    @Test
    fun `should update name of user and return updated user`() = runBlocking {
        val (id, name, updatedName) = Triple("test", "0", "1")
        val user = User(id, name = name)
        every {
            userService.updateName(
                any(),
                any()
            )
        } returns Tasks.forResult(user.copy(name = updatedName))

        val output = repository.updateName(id, updatedName)
        assertEquals(user.copy(name = updatedName).right(), output)
    }

    @Test
    fun `should update email of user and return updated user`() = runBlocking {
        val (id, email, updatedEmail) = Triple("test", "0", "1")
        val user = User(id, email = email)
        every {
            userService.updateEmail(
                any(),
                any()
            )
        } returns Tasks.forResult(user.copy(email = updatedEmail))

        val output = repository.updateEmail(id, updatedEmail)
        assertEquals(user.copy(email = updatedEmail).right(), output)
    }

    @Test
    fun `should change password of user and return updated user`() = runBlocking {
        val (id, password) = Pair("test", "test")
        val user = User(id)
        every { userService.changePassword(any(), any()) } returns Tasks.forResult(user)

        val output = repository.changePassword(id, password)
        assertEquals(user.right(), output)
    }

    @Test
    fun `should delete user and return id`() = runBlocking {
        val id = "test"
        every { userService.deleteUser(id) } returns Tasks.forResult(id)

        val output = repository.deleteUser(id)
        assertEquals(id.right(), output)
    }

}