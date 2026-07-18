package com.shreyashurakadli.engine.state.game

import com.shreyashurakadli.engine.state.player.Player

data class Game(
    val players: List<Player>,
    val currentTurnPlayerIdx: Int,
    val status: GameStatus
)