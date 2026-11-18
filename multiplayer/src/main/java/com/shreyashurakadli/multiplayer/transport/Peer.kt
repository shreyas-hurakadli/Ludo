package com.shreyashurakadli.multiplayer.transport

import com.shreyashurakadli.multiplayer.MessageCodec
import com.shreyashurakadli.multiplayer.Player

internal data class Peer(
    val id: String,
    val name: String
) {
    fun toPlayer(): Player = Player(id = id, name = name)

    fun toCodec(): String =
        "${MessageCodec.ITEM_SCOPE_START}" + "$id${MessageCodec.FIELD_SEPARATOR}$name" + "${MessageCodec.ITEM_SCOPE_END}"
}
