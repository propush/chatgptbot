package com.pushkin.openaibot

import com.pushkin.openaibot.openai.client.impl.OpenaiClientProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(OpenaiClientProperties::class)
class OpenaibotApplication

fun main(args: Array<String>) {
    runApplication<OpenaibotApplication>(*args)
}
