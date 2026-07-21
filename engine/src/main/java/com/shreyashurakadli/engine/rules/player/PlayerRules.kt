package com.shreyashurakadli.engine.rules.player

import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.player.Player

internal interface PlayerRules {
    fun updatePlayer(player: Player, diceValue: Int, piece: Piece, partCount: Int): Player
    fun allPlayerHaveWonStatus(players: List<Player>): Boolean
    fun playerHasWonStatus(player: Player): Boolean
    fun updatePlayerCapturedPiece(player: Player, otherPlayer: Player, piece: Piece, partCount: Int): Player
    fun initializePlayer(players: List<String>): List<Player>
    fun showAvailableOptions(player: Player, diceValue: Int, quadrantCount: Int): List<Piece>
}