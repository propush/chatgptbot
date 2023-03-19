package com.pushkin.openaibot.openai.client.impl

import com.pushkin.openaibot.openai.client.OpenaiClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.EnabledIf

@SpringBootTest
@EnabledIf(value = "#{'\${spring.profiles.active}' == 'inttest'}", loadContext = true)
class OpenaiClientImplTest {

    @Autowired
    private lateinit var openaiClient: OpenaiClient

    @Test
    fun fetchCompletion() {
        val prompt = "This is a test"
        val completion = openaiClient.fetchCompletion(prompt, 7) {
            println("completionChunk: $it")
        }
        assertEquals("This is a test sentence", completion.trim())
    }

    @Test
    fun fetchCompletionLong() {
        var chunkCounter = 0
        var finalChunk = -1
        val prompt = "What can you do?"
        val completion = openaiClient.fetchCompletion(prompt, 30) {
            println("completionChunk: $it")
            chunkCounter++
            if (!it.hasNext) {
                finalChunk = chunkCounter
            }
        }
        println("chunks: $chunkCounter, completion: $completion")
        assertTrue(completion.isNotBlank() && completion.length > 30)
        assertTrue(chunkCounter > 5)
        assertEquals(chunkCounter, finalChunk)
    }
}
