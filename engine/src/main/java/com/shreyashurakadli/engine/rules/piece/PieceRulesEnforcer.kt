package com.shreyashurakadli.engine.rules.piece

import com.shreyashurakadli.engine.rules.piece.position.PositionRulesEnforcer
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.piece.Status

internal class PieceRulesEnforcer(
    val piece: Piece,
    val positionRulesEnforcer: PositionRulesEnforcer
) : PieceRules {
    override fun updatePiece(diceValue: Int, playerCount: Int): Piece {
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

    private fun determineStatus(piece: Piece): Status =
        positionRulesEnforcer.let {
            when {
                it.isAtBasePosition(piece.position.tile) -> Status.InBase
                it.isAtFinalPosition(piece.position.tile) -> Status.Finished
                else -> Status.OnBoard
            }
        }
}