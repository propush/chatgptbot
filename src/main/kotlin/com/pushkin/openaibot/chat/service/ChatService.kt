package com.pushkin.openaibot.chat.service

import com.pushkin.openaibot.chat.client.ChatBot
import com.pushkin.openaibot.chat.client.mm.vo.IncomingMessageVO
import com.pushkin.openaibot.chat.client.mm.vo.MattermostAuthRq
import com.pushkin.openaibot.chat.client.mm.vo.OutgoingMessageVO
import com.pushkin.openaibot.chat.exception.ChatBotPostingFailedException
import com.pushkin.openaigptclient.openai.service.OpenaiService
import jakarta.annotation.PostConstruct
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val chatServiceProperties: ChatServiceProperties,
    private val chatBot: ChatBot<MattermostAuthRq, IncomingMessageVO, OutgoingMessageVO>,
    private val openaiService: OpenaiService
) : KLogging() {

    @PostConstruct
    fun init() {
        chatBot.registerMessageCallback(::onMessageReceived)
        logger.info { "ChatService initialized" }
    }

    private fun onMessageReceived(message: IncomingMessageVO) {
        logger.debug { "Message received: $message" }
        openaiService.query(message.text, chatServiceProperties.maxTokens) { response ->
            logger.debug { "Response received: $response" }
            val text = response.text + if (response.hasNext) "" else "<END>"
            try {
                chatBot.postMessage(OutgoingMessageVO(message.channel, text))
            } catch (e: ChatBotPostingFailedException) {
                logger.error {
                    "Failed to post message to channel ${message.channel}"
                }
            }
        }
    }

}
