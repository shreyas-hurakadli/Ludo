package com.shreyashurakadli.engine.rules.player

import com.shreyashurakadli.engine.rules.piece.PieceRulesEnforcer
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.player.Player
import com.shreyashurakadli.engine.state.player.PlayerStatus

internal class PlayerRulesEnforcer(
    private val pieceRulesEnforcer: PieceRulesEnforcer
) : PlayerRules {
    override fun updatePlayer(player: Player, diceValue: Int, piece: Piece): Player {
        val newPlayerPieces = updatePlayerPieces(
            pieceId = piece.id,
            pieces = player.pieces,
            diceValue = diceValue
        )

        val newStatus = determineStatus(pieces = newPlayerPieces)

        return player.copy(
            pieces = newPlayerPieces,
            status = newStatus
        )
    }

    override fun allPlayerHaveWonStatus(players: List<Player>): Boolean {
        players.forEach { player ->
            if (player.status == PlayerStatus.InProgress) {
                return false
            }
        }
        return true
    }

    override fun playerHasWonStatus(player: Player): Boolean =
        player.status == PlayerStatus.Won

    private fun determineStatus(pieces: List<Piece>): PlayerStatus =
        if (pieceRulesEnforcer.hasFinishedAllPieces(pieces)) {
            PlayerStatus.Won
        } else {
            PlayerStatus.InProgress
        }

    private fun updatePlayerPieces(
        pieceId: Int,
        pieces: List<Piece>,
        diceValue: Int
    ): List<Piece> {
        val piece: Piece =
            pieces.find { it.id == pieceId } ?: throw IllegalStateException("Piece not found")

        val newPiece = pieceRulesEnforcer.updatePiece(
            piece = piece,
            diceValue = diceValue,
            playerCount = pieces.size
        )

        val updatedPieces = pieces.map { piece ->
            if (piece.id == pieceId) {
                newPiece
            } else {
                piece
            }
        }

        return updatedPieces
    }
}