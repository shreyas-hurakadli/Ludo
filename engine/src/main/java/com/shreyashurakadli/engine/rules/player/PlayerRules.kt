package com.shreyashurakadli.engine.rules.player

import com.shreyashurakadli.engine.rules.common.DiceValue
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.player.Player
import com.shreyashurakadli.engine.state.player.PlayerName

internal interface PlayerRules {
    fun updatePlayer(player: Player, diceValue: DiceValue, piece: Piece, partCount: Int): Player
    fun playerHasWonStatus(player: Player): Boolean
    fun updatePlayerCapturedPiece(player: Player, otherPlayer: Player, piece: Piece, partCount: Int): Player
    fun initializePlayer(players: List<PlayerName>): List<Player>
    fun showAvailableOptions(player: Player, diceValue: DiceValue, quadrantCount: Int): List<Piece>
}