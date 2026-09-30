package com.shreyashurakadli.multiplayer.transport

import com.shreyashurakadli.multiplayer.Player

internal data class Peer(
    val id: String,
    val displayName: String,
    val advertisedName: String? = null,
    val clusterId: String? = null
) {
    fun toPlayer(): Player = Player(id = id, name = displayName)
}
