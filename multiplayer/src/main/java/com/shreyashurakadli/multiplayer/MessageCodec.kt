package com.shreyashurakadli.multiplayer

// Update the MessageEventHandler whenever this adds a new subclass
internal sealed class MessageCodec(val name: String) {
    companion object {
        internal const val DELIMITER = ';'
    }

    abstract fun encode(): String

    data class LobbySize(val count: UInt) : MessageCodec("lobby") {
        override fun encode(): String = "$name$DELIMITER$count"
    }
}
