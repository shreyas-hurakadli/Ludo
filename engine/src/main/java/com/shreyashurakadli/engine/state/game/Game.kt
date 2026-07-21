package com.shreyashurakadli.engine.state.game

import com.shreyashurakadli.engine.state.player.Player

@ConsistentCopyVisibility
data class Game internal constructor(
    val players: List<Player> = emptyList(),
    val currentTurnPlayerIdx: Int = -1,
    val status: GameStatus = GameStatus.InProgress
)