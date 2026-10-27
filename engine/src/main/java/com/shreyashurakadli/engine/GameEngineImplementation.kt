package com.shreyashurakadli.engine

import com.shreyashurakadli.engine.rules.common.DiceValue
import com.shreyashurakadli.engine.rules.game.GameRules
import com.shreyashurakadli.engine.state.game.Game
import com.shreyashurakadli.engine.state.piece.PieceId
import com.shreyashurakadli.engine.state.player.PlayerId
import com.shreyashurakadli.engine.state.player.PlayerName

class GameEngineImplementation internal constructor(
    private val gameRulesEnforcer: GameRules
) : GameEngine {
    private var _gameState: Game = Game()

    override val gameState: Game
        get() = _gameState

    private var quadrantCount: Int = 4

    private var initCompleted: Boolean = false

    override fun init(players: List<PlayerName>) {
        if (initCompleted) {
            return
        }

        quadrantCount = determineQuadrantCount(players.size)

        _gameState = gameRulesEnforcer.initializeGame(players)
        initCompleted = true
    }

    private fun determineQuadrantCount(noOfPlayers: Int): Int {
        require(value = noOfPlayers >= 2) {
            "Minimum number of players is 2"
        }

        return when (noOfPlayers) {
            in (2..4) -> 4
            else -> noOfPlayers
        }
    }

    override fun showAvailableOptions(diceValue: DiceValue): Pair<PlayerId, List<PieceId>> {
        check(value = initCompleted) {
            "Game has not been initialized"
        }

        val (player, pieces) = gameRulesEnforcer.showCurrentMoveOptions(
            gameState = gameState,
            diceValue = diceValue,
            quadrantCount = quadrantCount
        )
        return player.id to pieces.map { it.id }
    }

    override fun movePiece(pieceId: PieceId, diceValue: DiceValue) {
        check(value = initCompleted) {
            "Game has not been initialized"
        }

        val piece = _gameState.players[_gameState.currentTurnPlayerIdx].pieces.find { it.id == pieceId }
            ?: throw IllegalStateException("Piece not found")

        _gameState = gameRulesEnforcer.updateGameState(
            gameState = _gameState,
            diceValue = diceValue,
            piece = piece,
            partCount = quadrantCount
        )
    }
}