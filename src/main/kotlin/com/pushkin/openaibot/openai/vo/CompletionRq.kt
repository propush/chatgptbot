package com.pushkin.openaibot.openai.vo

import com.fasterxml.jackson.annotation.JsonProperty

data class CompletionRq(
    val prompt: String,

    val model: String,

    val stream: Boolean,

    @JsonProperty("max_tokens")
    val maxTokens: Int,

    val temperature: Int
)
