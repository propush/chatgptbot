package com.pushkin.openaibot.openai.vo

data class CompletionChunk(
    val text: String,
    val hasNext: Boolean
)
