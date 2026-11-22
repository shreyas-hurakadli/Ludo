package com.shreyashurakadli.netcode

import com.shreyashurakadli.multiplayer.transport.Peer

/**
 * A player in the game.
 * Mirrors :multiplayer's Peer so callers never name :multiplayer types.
 */
data class Player(
    val id: String,
    val name: String
)

internal fun Peer.toPlayer(): Player = Player(id = id, name = name)
