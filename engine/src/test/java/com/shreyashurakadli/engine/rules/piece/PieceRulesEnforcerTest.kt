package com.shreyashurakadli.engine.rules.piece

import com.shreyashurakadli.engine.rules.piece.position.PositionRulesEnforcer
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.piece.PieceStatus
import com.shreyashurakadli.engine.state.piece.position.Position
import com.shreyashurakadli.engine.state.piece.position.PositionStatus
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PieceRulesEnforcerTest {
    val positionRulesEnforcer = PositionRulesEnforcer()
    val pieceRulesEnforcer = PieceRulesEnforcer(positionRulesEnforcer)

    @Test
    fun updatePiece_AtBase() {
        val previousPiece = Piece(
            position = Position(
                part = 0,
                tile = -1,
                status = PositionStatus.Safe
            ),
            status = PieceStatus.InBase
        )
        val actualPiece = pieceRulesEnforcer.updatePiece(
            piece = previousPiece,
            diceValue = 1,
            playerCount = 4,
        )
        val expectedPiece = previousPiece
        assertEquals(expectedPiece, actualPiece)
    }
}