package com.numq.firebasechat.message

import arrow.core.right
import com.google.android.gms.tasks.Tasks
import com.numq.firebasechat.chat.Chat
import com.numq.firebasechat.chat.ChatApi
import com.numq.firebasechat.network.NetworkApi
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class MessageRepositoryTest {

    private lateinit var repository: MessageRepository

    @MockK
    private lateinit var networkService: NetworkApi

    @MockK
    private lateinit var chatService: ChatApi

    @MockK
    private lateinit var messageService: MessageApi

    @Before
    fun before() {
        MockKAnnotations.init(this)
        every { networkService.isAvailable } returns true
        repository = MessageRepository.Implementation(networkService, chatService, messageService)
    }

    @Test
    fun `should return latest messages`() = runBlocking {
        val (chatId, limit) = Pair("test", 10L)
        val input = arrayOfNulls<Message>(10)
            .mapIndexed { idx, _ -> Message(id = "$idx", chatId = chatId) }.asFlow()
        every { messageService.getLatestMessages(any(), any()) } returns input

        val output = repository.getLatestMessages(chatId, limit)
        assertEquals(input.right(), output)
    }

    @Test
    fun `should return messages`() = runBlocking {
        val (chatId, lastMessageId, limit) = Triple("test", "test", 10L)
        val input = arrayOfNulls<Message>(10)
            .mapIndexed { idx, _ -> Message(id = "$idx", chatId = chatId) }
        every { messageService.getMessages(any(), any(), any()) } returns Tasks.forResult(input)

        val output = repository.getMessages(chatId, lastMessageId, limit)
        assertEquals(input.right(), output)
    }

    @Test
    fun `should create message and return it`() = runBlocking {
        val (chatId, userId, text) = arrayOfNulls<String>(3).map { "test" }
        val chat = Chat(chatId)
        val message = Message(id = "test", chatId = chatId, senderId = userId, text = text)
        every { chatService.getChatById(any()) } returns flowOf(chat)
        every { chatService.updateChat(any()) } returns Tasks.forResult(chat)
        every { messageService.createMessage(any(), any(), any()) } returns Tasks.forResult(message)

        val output = repository.createMessage(chatId, userId, text)
        assertEquals(message.right(), output.map { it.copy(id = "test") })
    }

    @Test
    fun `should change message read status and return boolean`() = runBlocking {
        val id = "test"
        val chat = Chat(id)
        val message = Message(id = id)
        every { chatService.getChatById(any()) } returns flowOf(chat)
        every { chatService.updateChat(any()) } returns Tasks.forResult(chat)
        every { messageService.readMessage(any()) } returns Tasks.forResult(message)

        assertEquals(message.right(), repository.readMessage(id))
    }

    @Test
    fun `should delete message and return id`() = runBlocking {
        val id = "test"
        val chat = Chat(id)
        every { chatService.getChatById(any()) } returns flowOf(chat)
        every { chatService.updateChat(any()) } returns Tasks.forResult(chat)
        every {
            messageService.getMessages(
                any(),
                any(),
                any()
            )
        } returns Tasks.forResult(emptyList())
        every { messageService.deleteMessage(any()) } returns Tasks.forResult(Message(id = id))

        assertEquals(id.right(), repository.deleteMessage(id))
    }

}