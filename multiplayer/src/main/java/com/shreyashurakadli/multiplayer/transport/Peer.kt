package com.shreyashurakadli.multiplayer.transport

import com.shreyashurakadli.multiplayer.MessageCodec

data class Peer(
    val id: String,
    val name: String
) {
    fun toCodec(): String =
        "${MessageCodec.ITEM_SCOPE_START}" + "$id${MessageCodec.FIELD_SEPARATOR}$name" + "${MessageCodec.ITEM_SCOPE_END}"
}
