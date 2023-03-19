package com.pushkin.openaibot.openai.service.impl

import com.pushkin.openaibot.openai.client.OpenaiClient
import com.pushkin.openaibot.openai.service.OpenaiService
import com.pushkin.openaibot.openai.vo.CompletionChunk
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class OpenaiServiceImpl(
    private val openaiClient: OpenaiClient
) : OpenaiService, KLogging() {

    override fun query(
        prompt: String,
        maxTokens: Int,
        chunkCallback: (completionChunk: CompletionChunk) -> Unit
    ): String {
        val collector = StringBuffer()
        return openaiClient.fetchCompletion(prompt, maxTokens) { collectChunk(it, collector, chunkCallback) }
    }

    private fun collectChunk(
        completionChunk: CompletionChunk,
        collector: StringBuffer,
        chunkCallback: (completionChunk: CompletionChunk) -> Unit
    ) {
        collector.append(completionChunk.text)
        if (isChunkCompleted(completionChunk)) {
            val text = collector.toString().trim()
            collector.delete(0, collector.length)
            if (text.isNotBlank() || !completionChunk.hasNext) {
                logger.info { "Completed chunk: $text" }
                chunkCallback(completionChunk.copy(text = text))
            }
        }
    }

    private fun isChunkCompleted(completionChunk: CompletionChunk): Boolean =
        !completionChunk.hasNext || completionChunk.text.endsWith("\n")

}
