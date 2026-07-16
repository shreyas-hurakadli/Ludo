package com.shreyashurakadli.engine.rules.game

import com.shreyashurakadli.engine.state.game.Game
import com.shreyashurakadli.engine.state.piece.Piece

internal interface GameRules {
    fun updateGameState(gameState: Game, diceValue: Int, piece: Piece, quadrantCount: Int): Game
}