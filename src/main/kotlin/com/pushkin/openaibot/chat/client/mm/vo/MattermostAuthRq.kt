package com.pushkin.openaibot.chat.client.mm.vo

data class MattermostAuthRq(
    val action: String,
    val data: Data,
    val seq: Int
) {
    data class Data(
        val token: String
    )
}
