package com.pushkin.openaibot.openai.client

import com.pushkin.openaibot.openai.vo.CompletionChunk

interface OpenaiClient {

    fun fetchCompletion(
        prompt: String,
        maxTokens: Int,
        chunkCallback: (completionChunk: CompletionChunk) -> Unit
    ): String

}
