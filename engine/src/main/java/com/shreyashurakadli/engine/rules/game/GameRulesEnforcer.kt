package com.shreyashurakadli.engine.rules.game

import com.shreyashurakadli.engine.rules.player.PlayerRulesEnforcer
import com.shreyashurakadli.engine.state.game.Game
import com.shreyashurakadli.engine.state.game.GameStatus
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.player.Player

internal class GameRulesEnforcer(
    private val playerRulesEnforcer: PlayerRulesEnforcer
) : GameRules {
    override fun updateGameState(gameState: Game, diceValue: Int, piece: Piece): Game {
        require(value = diceValue in 1..6) {
            "Dice value must in the range [1, 6]. Received $diceValue"
        }

        return gameState.let {
            val currentPlayer = it.players[it.currentTurnPlayerIdx]

            val newPlayer = playerRulesEnforcer.updatePlayer(
                player = currentPlayer,
                diceValue = diceValue,
                piece = piece
            )

            val newPlayers = it.players.map { player ->
                if (player.id == newPlayer.id) {
                    newPlayer
                } else {
                    player
                }
            }

            val newStatus = determineStatus(players = newPlayers)

            val newTurn = updateCurrentTurn(
                players = newPlayers,
                currentTurn = it.currentTurnPlayerIdx,
                playerCount = it.players.size
            )

            Game(
                players = newPlayers,
                currentTurnPlayerIdx = newTurn,
                status = newStatus
            )
        }
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