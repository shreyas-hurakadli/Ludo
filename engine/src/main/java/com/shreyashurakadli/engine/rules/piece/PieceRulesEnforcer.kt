package com.shreyashurakadli.engine.rules.piece

import com.shreyashurakadli.engine.rules.position.PositionRules
import com.shreyashurakadli.engine.rules.position.converter.RelativeToAbsolutePositionConverter
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.piece.PieceStatus
import com.shreyashurakadli.engine.state.position.PositionStatus
import kotlin.math.abs

internal class PieceRulesEnforcer(
    private val positionRulesEnforcer: PositionRules,
    private val converter: RelativeToAbsolutePositionConverter
) : PieceRules {
    override fun updatePiece(piece: Piece, diceValue: Int, partCount: Int): Piece {
        val newPosition = positionRulesEnforcer.updatePosition(
            state = piece.position,
            diceValue = diceValue,
            quadrantCount = partCount
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

    override fun isPieceCaptured(
        piece: Piece,
        otherPiece: Piece,
        partCount: Int,
        playerPart: Int,
        otherPlayerPart: Int
    ): Boolean {
        if (piece.position.status == PositionStatus.Safe) {
            return false
        }

        val absPos = converter.calculateAbsolutePosition(
            position = piece.position,
            partCount = partCount,
            playerPart = playerPart
        )

        val otherAbsPos = converter.calculateAbsolutePosition(
            position = otherPiece.position,
            partCount = partCount,
            playerPart = otherPlayerPart
        )

        return absPos == otherAbsPos
    }

    override fun updateCapturedPiece(piece: Piece): Piece =
        piece.copy(
            position = positionRulesEnforcer.provideBasePosition(),
            status = PieceStatus.InBase
        )

    override fun canPiecesBeMoved(
        pieces: List<Piece>,
        diceValue: Int,
        quadrantCount: Int
    ): List<Piece> =
        pieces.filter { piece ->
            positionRulesEnforcer.canPositionBeValid(
                currentState = piece.position,
                diceValue = diceValue,
                quadrantCount = quadrantCount
            )
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