package com.numq.firebasechat.interactor

import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class UseCaseTest {

    @Test
    fun `should return exception`() = runBlocking {
        val exception = Exception("test")
        val result = object : UseCase<Unit, Unit>() {
            override suspend fun execute(arg: Unit) = exception.left()
        }
        result.invoke(Unit, onException = {
            assertIs<Exception>(it)
            assertEquals(exception, it)
        }, onResult = {
            assertIs<Unit>(it)
            assertEquals(Unit, it)
        })
    }

    @Test
    fun `should return result`() = runBlocking {
        val input = "test"
        val result = object : UseCase<String, String>() {
            override suspend fun execute(arg: String) = arg.right()
        }
        result.invoke(input, onException = {
            assertIs<Exception>(it)
        }, onResult = {
            assertIs<String>(it)
            assertEquals(input, it)
        })
    }

}