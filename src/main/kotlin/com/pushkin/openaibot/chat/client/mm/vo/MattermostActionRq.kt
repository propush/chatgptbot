package com.pushkin.openaibot.chat.client.mm.vo

import com.fasterxml.jackson.annotation.JsonProperty

data class MattermostActionRq(
    val action: String,
    val data: Data,
    val seq: Int
) {
    data class Data(
        @JsonProperty("channel_id")
        val channelId: String
    )
}
