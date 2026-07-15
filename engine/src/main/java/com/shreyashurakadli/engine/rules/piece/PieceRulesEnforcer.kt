package com.shreyashurakadli.engine.rules.piece

import com.shreyashurakadli.engine.rules.piece.position.PositionRules
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.piece.PieceStatus

internal class PieceRulesEnforcer(
    private val positionRulesEnforcer: PositionRules
) : PieceRules {
    override fun updatePiece(piece: Piece, diceValue: Int, playerCount: Int): Piece {
        val newPosition = positionRulesEnforcer.updatePosition(
            state = piece.position,
            diceValue = diceValue,
            playerCount = playerCount
        )

        val newStatus = determineStatus(tile = newPosition.tile)

        return piece.copy(
            position = newPosition,
            status = newStatus
        )
    }

    override fun hasFinishedAllPieces(pieces: List<Piece>): Boolean {
        var finishedPieces = 0
        pieces.forEach { piece ->
            if (piece.status == PieceStatus.Finished) {
                finishedPieces++
            }
        }
        return finishedPieces == pieces.size
    }

    private fun determineStatus(tile: Int): PieceStatus =
        positionRulesEnforcer.let {
            when {
                it.isAtBasePosition(tile) -> PieceStatus.InBase
                it.isAtFinalPosition(tile) -> PieceStatus.Finished
                else -> PieceStatus.OnBoard
            }
        }
}