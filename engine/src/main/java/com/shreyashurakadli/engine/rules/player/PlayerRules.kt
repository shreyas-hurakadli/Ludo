package com.shreyashurakadli.engine.rules.player

import com.shreyashurakadli.engine.state.player.Player

internal interface PlayerRules {
    fun updatePlayer(player: Player, diceValue: Int, pieceIdx: Int): Player
    fun allPlayerHaveWonStatus(players: List<Player>): Boolean
}