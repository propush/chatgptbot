package com.pushkin.openaibot.chat.service

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "chat")
data class ChatServiceProperties @ConstructorBinding constructor(
    val maxTokens: Int
)
