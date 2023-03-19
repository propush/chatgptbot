package com.pushkin.openaibot.openai.service

import com.pushkin.openaibot.openai.vo.CompletionChunk

interface OpenaiService {

    fun query(
        prompt: String,
        maxTokens: Int,
        chunkCallback: (completionChunk: CompletionChunk) -> Unit
    ): String

}
