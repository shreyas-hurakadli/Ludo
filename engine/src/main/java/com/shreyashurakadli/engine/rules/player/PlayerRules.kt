package com.shreyashurakadli.engine.rules.player

import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.player.Player

internal interface PlayerRules {
    fun updatePlayer(player: Player, diceValue: Int, piece: Piece, partCount: Int): Player
    fun allPlayerHaveWonStatus(players: List<Player>): Boolean
    fun playerHasWonStatus(player: Player): Boolean
}