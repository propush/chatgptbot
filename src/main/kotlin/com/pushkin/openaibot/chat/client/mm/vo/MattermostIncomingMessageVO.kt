package com.pushkin.openaibot.chat.client.mm.vo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MattermostIncomingMessageVO(
    val broadcast: Broadcast?,
    val data: Data,
    val event: String,
    val seq: Int
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Broadcast(
        @JsonProperty("channel_id")
        val channelId: String,
        @JsonProperty("connection_id")
        val connectionId: String,
        @JsonProperty("omit_users")
        val omitUsers: Any?,
        @JsonProperty("team_id")
        val teamId: String,
        @JsonProperty("user_id")
        val userId: String
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Data(
        @JsonProperty("channel_display_name")
        val channelDisplayName: String,
        @JsonProperty("channel_name")
        val channelName: String,
        @JsonProperty("channel_type")
        val channelType: String,
        val mentions: String?,
        val post: String,
        @JsonProperty("sender_name")
        val senderName: String?,
        @JsonProperty("set_online")
        val isOnline: Boolean,
        @JsonProperty("team_id")
        val teamId: String
    )
}
