package com.shreyashurakadli.netcode

import com.shreyashurakadli.multiplayer.ReceivedMessage

internal class MessageEventHandler {
    // Update this when ever MessageCodec adds a new subclass
    private val templates = listOf<MessageCodec>()

    fun toEvent(message: ReceivedMessage): MessageCodec? {
        val content = message.content
        val delimiterIndex = content.indexOf(MessageCodec.DELIMITER)
        if (delimiterIndex == -1) return null
        val name = content.substring(0, delimiterIndex)
        val payload = content.substring(delimiterIndex + 1)

        val template = templates.firstOrNull { it.name == name } ?: return null
        return decodePayload(template, payload)
    }

    private fun decodePayload(template: MessageCodec, payload: String): MessageCodec? = null
}
