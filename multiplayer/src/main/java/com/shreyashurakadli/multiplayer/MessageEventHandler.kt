package com.shreyashurakadli.multiplayer

import com.shreyashurakadli.multiplayer.transport.Peer

internal class MessageEventHandler {
    // Update this when ever MessageCodec adds a new subclass
    private val templates = listOf(
        MessageCodec.LobbySize(0U),
        MessageCodec.Peers(emptyList(), 0U)
    )

    fun toEvent(message: ReceivedMessage): MessageCodec? {
        val content = message.content
        val delimiterIndex = content.indexOf(MessageCodec.DELIMITER)
        if (delimiterIndex == -1) return null
        val name = content.substring(0, delimiterIndex)
        val payload = content.substring(delimiterIndex + 1)

        val template = templates.firstOrNull { it.name == name } ?: return null
        return decodePayload(template, payload)
    }

    private fun decodePayload(template: MessageCodec, payload: String): MessageCodec? =
        when (template) {
            is MessageCodec.LobbySize -> decodeLobbySize(payload)
            is MessageCodec.Peers -> decodePeers(payload)
        }

    private fun decodeLobbySize(payload: String): MessageCodec.LobbySize? {
        val count = payload.toUIntOrNull()?.takeIf { it in 2U..4U } ?: return null
        return MessageCodec.LobbySize(count)
    }

    private fun decodePeers(payload: String): MessageCodec.Peers? {
        val delimitedFields = payload.split(MessageCodec.DELIMITER)
        if (delimitedFields.size <= 1) return null

        val start = MessageCodec.ITEM_SCOPE_START
        val end = MessageCodec.ITEM_SCOPE_END

        // Peers
        val rawPeers = delimitedFields[0]
            .split(Regex("[$start$end]"))
            .filter { it.isNotEmpty() && it.isNotBlank() }

        val peers = rawPeers.map { peerString ->
            val fields = peerString.split(MessageCodec.FIELD_SEPARATOR)
            Peer(
                id = fields[0],
                name = fields[1]
            )
        }

        // Position
        val position = delimitedFields[1].toUIntOrNull() ?: 0U

        return MessageCodec.Peers(peers, position)
    }
}
