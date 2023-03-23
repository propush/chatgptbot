package com.pushkin.openaibot

import com.pushkin.openaibot.chat.client.mm.impl.MattermostChatBotProperties
import com.pushkin.openaibot.chat.service.ChatServiceProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(
    MattermostChatBotProperties::class,
    ChatServiceProperties::class,
)
class OpenaibotApplication

fun main(args: Array<String>) {
    runApplication<OpenaibotApplication>(*args)
}
