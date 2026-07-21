package com.shreyashurakadli.engine

import com.shreyashurakadli.engine.rules.game.GameRules
import com.shreyashurakadli.engine.state.game.Game

class GameEngineImplementation internal constructor(
    private val gameRulesEnforcer: GameRules
) : GameEngine {
    var gameState: Game = Game()
        private set

    private var quadrantCount: Int = 4

    private var initCompleted: Boolean = false

    override fun init(noOfPlayers: Int, players: List<String>) {
        if (initCompleted) {
            return
        }

        quadrantCount = determineQuadrantCount(noOfPlayers)

        gameState = gameRulesEnforcer.initializeGame(players)
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

    override fun showAvailableOptions(diceValue: Int): Pair<Int, List<Int>> {
        check(value = initCompleted) {
            "Game has not been initialized"
        }

        require(value = diceValue in 1..6) {
            "Invalid dice value"
        }

        val (player, pieces) = gameRulesEnforcer.showCurrentMoveOptions(
            gameState = gameState,
            diceValue = diceValue,
            quadrantCount = quadrantCount
        )
        return player.id to pieces.map { it.id }
    }

    override fun movePiece(pieceId: Int, diceValue: Int) {
        check(value = initCompleted) {
            "Game has not been initialized"
        }

        require(value = diceValue in 1..6) {
            "Invalid dice value"
        }

        val piece = gameState.players[gameState.currentTurnPlayerIdx].pieces.find { it.id == pieceId }
            ?: throw IllegalStateException("Piece not found")

        gameState = gameRulesEnforcer.updateGameState(
            gameState = gameState,
            diceValue = diceValue,
            piece = piece,
            partCount = quadrantCount
        )
    }

    override fun isGameCompleted(): Boolean {
        check(value = initCompleted) {
            "Game has not been initialized"
        }

        return gameRulesEnforcer.isGameCompleted(gameState)
    }
}