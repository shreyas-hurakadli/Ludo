package com.shreyashurakadli.engine.rules.player

import com.shreyashurakadli.engine.rules.piece.PieceRules
import com.shreyashurakadli.engine.rules.position.common.GetBasePositionPiece
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.player.Player
import com.shreyashurakadli.engine.state.player.PlayerStatus

internal class PlayerRulesEnforcer(
    private val pieceRulesEnforcer: PieceRules
) : PlayerRules {
    override fun updatePlayer(
        player: Player,
        diceValue: Int,
        piece: Piece,
        partCount: Int
    ): Player {
        val newPlayerPieces = updatePlayerPieces(
            pieceId = piece.id,
            pieces = player.pieces,
            diceValue = diceValue,
            partCount = partCount
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

    override fun updatePlayerCapturedPiece(
        player: Player,
        otherPlayer: Player,
        piece: Piece,
        partCount: Int,
    ): Player {
        for (otherPiece in otherPlayer.pieces) {
            val isCaptured = pieceRulesEnforcer.isPieceCaptured(
                piece = piece,
                otherPiece = otherPiece,
                partCount = partCount,
                playerPart = player.id,
                otherPlayerPart = otherPlayer.id
            )

            if (isCaptured) {
                val newPieces = otherPlayer.pieces.map {
                    if (otherPiece.id == it.id) {
                        pieceRulesEnforcer.updateCapturedPiece(otherPiece)
                    } else {
                        it
                    }
                }

                return otherPlayer.copy(
                    pieces = newPieces,
                    status = determineStatus(pieces = newPieces)
                )
            }
        }

        return otherPlayer
    }

    private fun determineStatus(pieces: List<Piece>): PlayerStatus =
        if (pieceRulesEnforcer.hasFinishedAllPieces(pieces)) {
            PlayerStatus.Won
        } else {
            PlayerStatus.InProgress
        }

    private fun updatePlayerPieces(
        pieceId: Int,
        pieces: List<Piece>,
        diceValue: Int,
        partCount: Int
    ): List<Piece> {
        val piece: Piece =
            pieces.find { it.id == pieceId } ?: throw IllegalStateException("Piece not found")

        val newPiece = pieceRulesEnforcer.updatePiece(
            piece = piece,
            diceValue = diceValue,
            partCount = partCount
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