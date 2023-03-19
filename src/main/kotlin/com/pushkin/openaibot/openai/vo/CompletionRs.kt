package com.pushkin.openaibot.openai.vo

import com.fasterxml.jackson.annotation.JsonProperty

sealed class CompletionResponse{
    data class CompletionRs(
        val choices: List<Choice>,
        val created: Int,
        val id: String,
        val model: String,

        @JsonProperty("object")
        val theObject: String
    ): CompletionResponse()

    object CompletionDone : CompletionResponse()

}
