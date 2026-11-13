package com.shreyashurakadli.multiplayer

import com.shreyashurakadli.multiplayer.transport.Peer

// Update the MessageEventHandler whenever this adds a new subclass
internal sealed class MessageCodec(val name: String) {
    companion object {
        internal const val DELIMITER = ';'
        internal const val ITEM_SCOPE_START = '('
        internal const val ITEM_SCOPE_END = ')'
        internal const val FIELD_SEPARATOR = ','
    }

    abstract fun encode(): String

    data class LobbySize(val count: UInt) : MessageCodec("lobby") {
        override fun encode(): String = "$name$DELIMITER$count"
    }

    data class Peers(val peers: List<Peer>, val position: UInt) : MessageCodec("peers") {
        override fun encode(): String {
            val builder = StringBuilder()

            // Add codec name
            builder.append(name)
            builder.append(DELIMITER)

            // Add peers
            peers.forEach { peer ->
                builder.append(peer.toCodec())
            }
            builder.append(DELIMITER)

            // Add the position of the peer
            builder.append(position.toString())

            return builder.toString()
        }
    }
}