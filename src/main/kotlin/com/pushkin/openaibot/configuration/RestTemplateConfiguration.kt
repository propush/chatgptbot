package com.pushkin.openaibot.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfiguration {

    @Bean
    fun restTemplate() = RestTemplate(getClientHttpRequestFactory())

    private fun getClientHttpRequestFactory() =
        SimpleClientHttpRequestFactory().apply {
            setConnectTimeout(5000)
            setReadTimeout(10000)
        }

}
