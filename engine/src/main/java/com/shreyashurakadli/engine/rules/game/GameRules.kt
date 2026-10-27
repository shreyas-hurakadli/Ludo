package com.shreyashurakadli.engine.rules.game

import com.shreyashurakadli.engine.rules.common.DiceValue
import com.shreyashurakadli.engine.state.game.Game
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.player.Player
import com.shreyashurakadli.engine.state.player.PlayerName

internal interface GameRules {
    fun updateGameState(gameState: Game, diceValue: DiceValue, piece: Piece, partCount: Int): Game
    fun updateCapture(players: List<Player>, player: Player, piece: Piece, partCount: Int): List<Player>
    fun isGameCompleted(gameState: Game): Boolean
    fun initializeGame(players: List<PlayerName>): Game
    fun showCurrentMoveOptions(gameState: Game, diceValue: DiceValue, quadrantCount: Int): Pair<Player, List<Piece>>
    fun skipCurrentTurn(gameState: Game): Game
}
