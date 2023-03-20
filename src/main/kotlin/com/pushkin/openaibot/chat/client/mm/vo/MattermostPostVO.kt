package com.pushkin.openaibot.chat.client.mm.vo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MattermostPostVO(
    @JsonProperty("channel_id")
    val channelId: String,
    @JsonProperty("create_at")
    val createAt: Long?,
    @JsonProperty("delete_at")
    val deleteAt: Int?,
    @JsonProperty("edit_at")
    val editAt: Int?,
    val hashtags: String?,
    val id: String,
    @JsonProperty("is_pinned")
    val isPinned: Boolean?,
    @JsonProperty("last_reply_at")
    val lastReplyAt: Int?,
    val message: String?,
    @JsonProperty("original_id")
    val originalId: String?,
    val participants: Any?,
    @JsonProperty("pending_post_id")
    val pendingPostId: String?,
    val props: Props?,
    @JsonProperty("reply_count")
    val replyCount: Int?,
    @JsonProperty("root_id")
    val rootId: String?,
    val type: String,
    @JsonProperty("update_at")
    val updateAt: Long?,
    @JsonProperty("user_id")
    val userId: String
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Props(
        @JsonProperty("disable_group_highlight")
        val disableGroupHighlight: Boolean?
    )
}
