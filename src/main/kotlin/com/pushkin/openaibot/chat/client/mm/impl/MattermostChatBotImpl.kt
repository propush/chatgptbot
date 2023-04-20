package com.pushkin.openaibot.chat.client.mm.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.pushkin.openaibot.chat.client.ChatBot
import com.pushkin.openaibot.chat.client.mm.vo.*
import com.pushkin.openaibot.chat.exception.ChatBotPostingFailedException
import jakarta.annotation.PostConstruct
import mu.KLogging
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.socket.*
import org.springframework.web.socket.client.WebSocketClient
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

@Service
class MattermostChatBotImpl(
    private val mattermostChatBotProperties: MattermostChatBotProperties,
    private val objectMapper: ObjectMapper,
    private val restTemplate: RestTemplate
) : ChatBot<MattermostAuthRq, IncomingMessageVO, OutgoingMessageVO>, WebSocketHandler, KLogging() {

    companion object {
        private const val DEFAULT_RECONNECT_DELAY_MS = 5000L
        private const val MM_API_BASE = "/api/v4"
        private const val MM_WSS = "/websocket"
        private const val MM_POSTS_PATH = "/posts"
    }

    private val sequence = AtomicInteger(0)
    private val wsClient: WebSocketClient = StandardWebSocketClient()

    private val wssUri =
        "${mattermostChatBotProperties.url.replace("http[s?]://".toRegex(), "wss://")}${MM_API_BASE}${MM_WSS}"
    private val authHeaders = HttpHeaders().apply {
        accept = listOf(MediaType.APPLICATION_JSON)
        contentType = MediaType.APPLICATION_JSON
        set("Authorization", "Bearer ${mattermostChatBotProperties.token}")
    }

    private var webSocketSession: WebSocketSession? = null
    private var messageCallback: ((IncomingMessageVO) -> Unit)? = null
    private lateinit var authentication: MattermostAuthRq

    @PostConstruct
    fun init() {
        logger.debug { "Initializing Mattermost chat bot, properties: $mattermostChatBotProperties" }
        connect(
            MattermostAuthRq(
                action = "authentication_challenge",
                data = MattermostAuthRq.Data(
                    token = mattermostChatBotProperties.token
                ),
                seq = sequence.incrementAndGet()
            )
        )
    }

    private fun onIncomingMessageReceived(message: MattermostIncomingMessageVO) {
        logger.debug { "Incoming message received: $message" }
        if (message.event == "posted") {
            if (!mattermostChatBotProperties.channels.contains(message.data.channelName)) {
                logger.warn { "Message posted to channel ${message.data.channelName} is not in the list of channels to listen to" }
                return
            }
            try {
                val post = objectMapper.readValue(message.data.post, MattermostPostVO::class.java)
                logger.debug { "Post received: $post" }
                val messageText = post.message ?: ""
                if (mattermostChatBotProperties.mentions.any(messageText::contains)) {
                    logger.debug { "Mention found in message" }
                    sendTyping(post.channelId)
                    invokeMessageCallback(post.channelId, messageText)
                } else {
                    logger.debug { "Mention not found in message" }
                }
            } catch (e: Exception) {
                logger.error {
                    "Error while parsing post: ${e.message}"
                }
            }
        }
    }

    private fun invokeMessageCallback(channelId: String, messageText: String) {
        val callbackThread = thread {
            messageCallback?.invoke(
                IncomingMessageVO(
                    channelId,
                    stripStartingMentions(messageText)
                )
            )
        }
        sendTypingWhileThreadActive(channelId, callbackThread)
    }

    private fun sendTypingWhileThreadActive(channelId: String, callbackThread: Thread) {
        while (callbackThread.isAlive) {
            sendTyping(channelId)
            Thread.sleep(5000)
        }
    }

    private fun stripStartingMentions(message: String): String {
        mattermostChatBotProperties.mentions.filter { it.startsWith('@') }.forEach {
            if (message.startsWith(it)) {
                return message.substring(it.length).trimStart()
            }
        }
        return message
    }

    override fun connect(authentication: MattermostAuthRq) {
        this.authentication = authentication
        connectWs()
    }

    private fun toWsMessage(authentication: MattermostAuthRq): WebSocketMessage<*> {
        val data = objectMapper.writeValueAsString(authentication)
        return TextMessage(data)
    }

    private fun connectWs(): CompletableFuture<WebSocketSession> {
        logger.info { "Connecting to $wssUri" }
        webSocketSession = null
        return wsClient.execute(this, wssUri)
    }

    override fun registerMessageCallback(callback: (IncomingMessageVO) -> Unit) {
        messageCallback = callback
    }

    override fun postMessage(message: OutgoingMessageVO) {
        sendTyping(message.channel)
        val request = HttpEntity<MattermostPostRq>(
            MattermostPostRq(
                channelId = message.channel,
                message = message.text
            ),
            authHeaders
        )
        try {
            val responseEntity = restTemplate.exchange(
                "${mattermostChatBotProperties.url}${MM_API_BASE}${MM_POSTS_PATH}",
                HttpMethod.POST,
                request,
                MattermostPostVO::class.java
            )
            if (responseEntity.statusCode.is2xxSuccessful) {
                logger.debug { "Message posted successfully" }
            } else {
                logger.error { "Error while posting message: ${responseEntity.statusCode}" }
                throw ChatBotPostingFailedException("Error while posting message: ${responseEntity.statusCode}")
            }
        } catch (e: Exception) {
            logger.error { "Error while posting message: ${e.message}" }
            throw ChatBotPostingFailedException("Error while posting message: ${e.message}")
        }
    }

    private fun sendTyping(channelId: String) {
        if (webSocketSession == null) {
            logger.error { "Websocket session is null, cannot send data" }
            return
        }
        val rq = MattermostActionRq(
            action = "user_typing",
            data = MattermostActionRq.Data(channelId = channelId),
            seq = sequence.incrementAndGet()
        )
        webSocketSession?.sendMessage(
            TextMessage(
                objectMapper.writeValueAsString(rq)
            )
        )
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info { "Connection established" }
        webSocketSession = session
        logger.trace { "Sending authentication: $authentication" }
        webSocketSession?.sendMessage(toWsMessage(authentication))
        logger.info { "Authentication sent" }
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        logger.debug { "Message received: ${message.payload}" }
        val data = message.payload.toString()
        try {
            val incomingMessage = objectMapper.readValue(data, MattermostIncomingMessageVO::class.java)
            onIncomingMessageReceived(incomingMessage)
        } catch (e: Exception) {
            logger.trace { "$e" }
            try {
                val incomingMessage = objectMapper.readValue(data, MattermostStatusRs::class.java)
                if (incomingMessage.status != "OK") {
                    logger.error { "Bad status ${incomingMessage.status} received, reconnecting" }
                    webSocketSession?.close()
                    connectWs()
                    sleep()
                } else {
                    logger.info { "Status successful" }
                }
            } catch (e: Exception) {
                logger.trace { "$e" }
                logger.debug { "Unknown message type" }
            }
        }
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        logger.error { "Transport error: ${exception.message}" }
        connectWs()
        sleep()
    }

    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
        logger.info { "Connection closed: ${closeStatus.reason}, $closeStatus" }
        connectWs()
        sleep()
    }

    private fun sleep() {
        try {
            Thread.sleep(DEFAULT_RECONNECT_DELAY_MS)
        } catch (e: InterruptedException) {
            logger.error { "Interrupted while waiting for reconnect" }
        }
    }

    override fun supportsPartialMessages(): Boolean = false
}
