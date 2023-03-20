package com.pushkin.openaibot.openai.client.impl

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.client.ExpectedCount
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withException
import org.springframework.web.client.RestTemplate
import java.io.IOException


@SpringBootTest
class OpenaiClientImplUnitTest {

    @Autowired
    private lateinit var openaiClientImpl: OpenaiClientImpl

    @Autowired
    private lateinit var restTemplate: RestTemplate

    private lateinit var mockServer: MockRestServiceServer

    @BeforeEach
    fun setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate)
    }

    @Test
    fun fetchCompletionTestServerException() {
        mockServer.expect(
            ExpectedCount.times(3),
            requestTo("https://api.openai.com/v1/chat/completions")
        ).andRespond(withException(IOException("timeout")))
        val completion = openaiClientImpl.fetchCompletion("test", 10) {}
        println("completion: $completion")
        assertTrue(completion.startsWith("Sorry"))
        mockServer.verify()
    }
}
