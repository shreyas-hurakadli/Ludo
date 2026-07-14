package com.shreyashurakadli.engine.rules.player

import com.shreyashurakadli.engine.rules.piece.PieceRulesEnforcer
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.player.Player
import com.shreyashurakadli.engine.state.player.PlayerStatus

internal class PlayerRulesEnforcer(
    private val pieceRulesEnforcer: PieceRulesEnforcer
) : PlayerRules {
    override fun updatePlayer(player: Player, diceValue: Int, pieceIdx: Int): Player {
        val newPlayerPieces = updatePlayerPieces(
            pieceIdx = pieceIdx,
            pieces = player.pieces,
            diceValue = diceValue
        )

        val newStatus = determineStatus(pieces = newPlayerPieces)

        return player.copy(
            pieces = newPlayerPieces,
            status = newStatus
        )
    }

    private fun determineStatus(pieces: List<Piece>): PlayerStatus =
        if (pieceRulesEnforcer.hasFinishedAllPieces(pieces)) {
            PlayerStatus.Won
        } else {
            PlayerStatus.InProgress
        }

    private fun updatePlayerPieces(
        pieceIdx: Int,
        pieces: List<Piece>,
        diceValue: Int
    ): List<Piece> {
        val newPiece = pieceRulesEnforcer.updatePiece(
            piece = pieces[pieceIdx],
            diceValue = diceValue,
            playerCount = pieces.size
        )

        val updatedPieces = pieces.mapIndexed { index, piece ->
            if (index == pieceIdx) {
                newPiece
            } else {
                piece
            }
        }

        return updatedPieces
    }
}