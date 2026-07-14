package com.shreyashurakadli.engine.rules.player

import com.shreyashurakadli.engine.rules.piece.PieceRulesEnforcer
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.player.Player
import com.shreyashurakadli.engine.state.player.PlayerStatus

internal class PlayerRulesEnforcer(
    private val pieceRulesEnforcer: PieceRulesEnforcer
) : PlayerRules {
    override fun updatePlayer(player: Player, diceValue: Int): Player {
        val newPlayerPieces = updatePlayerPieces(
            playerId = player.id,
            pieces = player.pieces,
            diceValue = diceValue
        )

        val newStatus = determineStatus(player = player)

        return player.copy(
            pieces = newPlayerPieces,
            status = newStatus
        )
    }

    private fun determineStatus(player: Player): PlayerStatus =
        when {
            pieceRulesEnforcer.hasFinishedAllPieces(
                pieces = player.pieces,
                pieceCount = PlayerConstants.PIECES_PER_PLAYER
            ) -> PlayerStatus.Won

            else -> PlayerStatus.InProgress
        }

    private fun updatePlayerPieces(
        playerId: Int,
        pieces: List<Piece>,
        diceValue: Int
    ): List<Piece> {
        val newPiece = pieceRulesEnforcer.updatePiece(
            piece = pieces[playerId],
            diceValue = diceValue,
            playerCount = pieces.size
        )

        val updatedPieces = pieces.mapIndexed { index, piece ->
            if (index == playerId) {
                newPiece
            } else {
                piece
            }
        }

        return updatedPieces
    }
}