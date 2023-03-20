package com.pushkin.openaibot.chat.client.mm.vo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MattermostStatusRs(
    val status: String,
    @JsonProperty("seq_reply") val sequence: Int,
)
