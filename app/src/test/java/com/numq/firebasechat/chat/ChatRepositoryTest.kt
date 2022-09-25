package com.numq.firebasechat.chat

import arrow.core.Either
import arrow.core.right
import com.google.android.gms.tasks.Tasks
import com.numq.firebasechat.network.NetworkApi
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ChatRepositoryTest {

    private lateinit var repository: ChatRepository

    @MockK
    private lateinit var networkService: NetworkApi

    @MockK
    private lateinit var chatService: ChatApi

    @Before
    fun before() {
        MockKAnnotations.init(this)
        every { networkService.isAvailable } returns true
        repository = ChatData(networkService, chatService)
    }

    @Test
    fun `should return multiple chats`() = runBlocking {
        val (userId, lastChatId, limit) = Triple<String, String?, Long>("id", null, 10L)
        val chats = arrayOfNulls<Chat>(10)
            .mapIndexed { idx, _ -> Chat(id = idx.toString()) }
            .asFlow()
        every {
            chatService.getChats(
                userId = userId,
                lastChatId = lastChatId,
                limit = limit
            )
        } returns chats
        val output = repository.getChats(userId, lastChatId, limit)
        assertIs<Either<Exception, Flow<Chat>>>(output)
        assertEquals(chats.right(), output)
    }

    @Test
    fun `should return single chat by id`() = runBlocking {
        val id = "id"
        val chat = Chat(id = id)
        every { chatService.getChatById(id) } returns flowOf(chat)
        val output = repository.getChatById(id)
        assertIs<Either<Exception, Flow<Chat>>>(output)
        assertEquals(listOf(chat).right(), output.map { it.toList() })
    }

    @Test
    fun `should create chat and return it`() = runBlocking {
        val (userId, anotherId) = Pair("id1", "id2")
        val chat = Chat(
            typingUserIds = listOf(
                userId,
                anotherId
            ).sortedDescending()
        )
        every {
            chatService.createChat(
                userId = userId,
                anotherId = anotherId
            )
        } returns Tasks.forResult(chat)
        val output = repository.createChat(userId, anotherId)
        assertIs<Either<Exception, Chat>>(output)
        assertEquals(chat.right(), output)
    }

    @Test
    fun `should update chat and return it`() = runBlocking {
        val (name, updatedName) = Pair("0", "1")
        val chat = Chat(name = name)
        every { chatService.updateChat(chat) } returns Tasks.forResult(chat.copy(name = updatedName))
        val output = repository.updateChat(chat)
        assertIs<Either<Exception, Chat>>(output)
        assertEquals(chat.copy(name = updatedName).right(), output)
    }

    @Test
    fun `should delete chat and return id`() = runBlocking {
        val id = "id"
        val task = Tasks.forResult(mockk<Void>())
        every { chatService.deleteChat(id) } returns task
        val output = repository.deleteChat(id)
        assertIs<Either<Exception, String>>(output)
        assertEquals(id.right(), output)
    }

}