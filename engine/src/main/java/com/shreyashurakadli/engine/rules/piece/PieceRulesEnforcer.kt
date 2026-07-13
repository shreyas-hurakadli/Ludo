package com.shreyashurakadli.engine.rules.piece

import com.shreyashurakadli.engine.rules.piece.position.PositionRulesEnforcer
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.piece.PieceStatus

internal class PieceRulesEnforcer(
    private val positionRulesEnforcer: PositionRulesEnforcer
) : PieceRules {
    override fun updatePiece(piece: Piece, diceValue: Int, playerCount: Int): Piece {
        val newPosition = positionRulesEnforcer.updatePosition(
            state = piece.position,
            diceValue = diceValue,
            playerCount = playerCount
        )

        val newStatus = determineStatus(piece = piece)

        return Piece(
            position = newPosition,
            status = newStatus
        )
    }

    private fun determineStatus(piece: Piece): PieceStatus =
        positionRulesEnforcer.let {
            when {
                it.isAtBasePosition(piece.position.tile) -> PieceStatus.InBase
                it.isAtFinalPosition(piece.position.tile) -> PieceStatus.Finished
                else -> PieceStatus.OnBoard
            }
        }
}