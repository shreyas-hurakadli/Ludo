package com.shreyashurakadli.engine.state.game

import com.shreyashurakadli.engine.state.player.Player

internal data class Game(
    val players: List<Player>,
    val currentTurn: Player,
    val status: Status
)