package com.shreyashurakadli.engine.rules.game

import com.shreyashurakadli.engine.rules.player.PlayerRulesEnforcer
import com.shreyashurakadli.engine.state.game.Game
import com.shreyashurakadli.engine.state.game.GameStatus
import com.shreyashurakadli.engine.state.player.Player

internal class GameRulesEnforcer(
    private val playerRulesEnforcer: PlayerRulesEnforcer
) : GameRules {
    override fun updateGameState(gameState: Game, diceValue: Int): Game =
        gameState.let {
            val currentPlayer = it.players[it.currentTurnPlayerIdx]

            playerRulesEnforcer.updatePlayer(
                player = currentPlayer,
                diceValue = diceValue,
                pieceIdx = it.currentTurnPlayerIdx
            )

            val newStatus = determineStatus(players = it.players)
            val newTurn = updateCurrentTurn(
                players = it.players,
                currentTurn = it.currentTurnPlayerIdx,
                playerCount = it.players.size
            )

            Game(
                players = it.players,
                currentTurnPlayerIdx = newTurn,
                status = newStatus
            )
        }

    private fun determineStatus(players: List<Player>): GameStatus =
        playerRulesEnforcer.let {
            when {
                it.allPlayerHaveWonStatus(players) -> GameStatus.Finished
                else -> GameStatus.InProgress
            }
        }

    private fun updateCurrentTurn(players: List<Player>, currentTurn: Int, playerCount: Int): Int {
        var idx = (currentTurn + 1) % playerCount

        // Iterate through all players and check their status
        while (idx != currentTurn) {
            // Return the first player who has not won (i.e., still can play)
            if (!playerRulesEnforcer.playerHasWonStatus(player = players[idx])) {
                return idx
            }
            idx = (idx + 1) % playerCount
        }

        return idx
    }
}