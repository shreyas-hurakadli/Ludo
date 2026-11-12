package com.shreyashurakadli.multiplayer.transport

import com.shreyashurakadli.multiplayer.Player

internal data class Peer(
    val id: String,
    val name: String
) {
    fun toPlayer(): Player = Player(id = id, name = name)
}
