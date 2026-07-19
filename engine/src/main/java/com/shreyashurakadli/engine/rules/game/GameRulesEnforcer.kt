package com.shreyashurakadli.engine.rules.game

import com.shreyashurakadli.engine.rules.piece.common.GetUpdatedPiece
import com.shreyashurakadli.engine.rules.piece.common.PieceHasFinishedStatus
import com.shreyashurakadli.engine.rules.player.PlayerRules
import com.shreyashurakadli.engine.state.game.Game
import com.shreyashurakadli.engine.state.game.GameStatus
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.player.Player

internal class GameRulesEnforcer(
    private val playerRulesEnforcer: PlayerRules,
    private val pieceHasFinishedStatus: PieceHasFinishedStatus,
    private val getUpdatedPiece: GetUpdatedPiece,
) : GameRules {
    override fun updateGameState(
        gameState: Game,
        diceValue: Int,
        piece: Piece,
        partCount: Int
    ): Game {
        require(value = diceValue in 1..6) {
            "Dice value must in the range [1, 6]. Received $diceValue"
        }

        return gameState.let {
            val currentPlayer = it.players[it.currentTurnPlayerIdx]

            val newPlayer = playerRulesEnforcer.updatePlayer(
                player = currentPlayer,
                diceValue = diceValue,
                piece = piece,
                partCount = partCount
            )

            val updatedPiece = getUpdatedPiece(newPlayer, piece.id)

            val newPlayers = it.players.map { player ->
                if (player.id == newPlayer.id) {
                    newPlayer
                } else {
                    player
                }
            }

            // Check and update piece capture
            val playersAfterCaptureUpdate = updateCapture(
                players = newPlayers,
                player= newPlayer,
                piece = updatedPiece,
                partCount = partCount
            )

            val newStatus = determineStatus(players = playersAfterCaptureUpdate)

            val newTurn = updateCurrentTurn(
                players = playersAfterCaptureUpdate,
                currentTurn = it.currentTurnPlayerIdx,
                updatedPiece = updatedPiece,
                diceValue = diceValue,
                hasCapturedPiece = newPlayers != playersAfterCaptureUpdate
            )

            Game(
                players = playersAfterCaptureUpdate,
                currentTurnPlayerIdx = newTurn,
                status = newStatus
            )
        }
    }

    override fun updateCapture(players: List<Player>, player: Player, piece: Piece, partCount: Int): List<Player> =
        players.map {
            if (player == it) {
                it
            } else {
                playerRulesEnforcer.updatePlayerCapturedPiece(
                    player = player,
                    otherPlayer = it,
                    piece = piece,
                    partCount = partCount
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

    private fun updateCurrentTurn(
        players: List<Player>,
        currentTurn: Int,
        updatedPiece: Piece,
        diceValue: Int,
        hasCapturedPiece: Boolean
    ): Int {
        if (shouldCurrentPlayerRepeatTurnDiceValue(players[currentTurn], diceValue)) {
            return currentTurn
        }

        if (shouldCurrentPlayerRepeatTurnFinishedPiece(players[currentTurn], updatedPiece)) {
            return currentTurn
        }

        if (hasCapturedPiece) {
            return currentTurn
        }

        return findNextPlayerIndex(
            players = players,
            currentTurn = currentTurn,
            playerCount = players.size
        )
    }

    private fun shouldCurrentPlayerRepeatTurnDiceValue(player: Player, diceValue: Int): Boolean =
        diceValue == 6 && !playerRulesEnforcer.playerHasWonStatus(player)

    private fun shouldCurrentPlayerRepeatTurnFinishedPiece(
        player: Player,
        updatedPiece: Piece
    ): Boolean =
        pieceHasFinishedStatus(updatedPiece) && !playerRulesEnforcer.playerHasWonStatus(player)

    private fun findNextPlayerIndex(
        players: List<Player>,
        currentTurn: Int,
        playerCount: Int
    ): Int {
        var idx = (currentTurn + 1) % playerCount

        // Iterate through all players and check their status
        while (idx != currentTurn) {
            // Return the first player who has not won (i.e., still can play)
            if (!playerRulesEnforcer.playerHasWonStatus(player = players[idx])) {
                return idx
            }
            idx = (idx + 1) % playerCount
        }

        // Return an invalid index if all players have won
        return -1
    }
}