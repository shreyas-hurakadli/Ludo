package com.shreyashurakadli.engine.rules.game

import com.shreyashurakadli.engine.state.game.Game
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.player.Player

internal interface GameRules {
    fun updateGameState(gameState: Game, diceValue: Int, piece: Piece, partCount: Int): Game
    fun updateCapture(players: List<Player>, player: Player, piece: Piece, partCount: Int): List<Player>
}