package com.pushkin.openaibot.openai.vo

import com.fasterxml.jackson.annotation.JsonProperty

data class Choice(
    val text: String?,

    @JsonProperty("finish_reason")
    val finishReason: String?,

    val index: Int
)
