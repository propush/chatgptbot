package com.pushkin.openaibot.chat.client.mm.vo

import com.fasterxml.jackson.annotation.JsonProperty

data class MattermostPostRq(
    @JsonProperty("channel_id")
    val channelId: String,
    val message: String
)
