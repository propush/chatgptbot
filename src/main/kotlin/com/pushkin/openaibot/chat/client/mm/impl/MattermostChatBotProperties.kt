package com.pushkin.openaibot.chat.client.mm.impl

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "bot.mattermost")
data class MattermostChatBotProperties @ConstructorBinding constructor(
    val url: String,
    val token: String,
    val mentions: List<String>,
    val channels: List<String>
)
