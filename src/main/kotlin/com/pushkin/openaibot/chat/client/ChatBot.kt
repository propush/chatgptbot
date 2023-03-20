package com.pushkin.openaibot.chat.client

import com.pushkin.openaibot.chat.exception.ChatBotConnectionFailedException
import com.pushkin.openaibot.chat.exception.ChatBotPostingFailedException

interface ChatBot<AUTH, INC, OUT> {

    @Throws(ChatBotConnectionFailedException::class)
    fun connect(authentication: AUTH)

    fun registerMessageCallback(callback: (INC) -> Unit)

    @Throws(ChatBotPostingFailedException::class)
    fun postMessage(message: OUT)

}
