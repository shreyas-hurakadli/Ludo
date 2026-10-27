package com.shreyashurakadli.engine.rules.piece

import com.shreyashurakadli.engine.rules.common.DiceValue
import com.shreyashurakadli.engine.rules.position.PositionRulesEnforcer
import com.shreyashurakadli.engine.rules.position.common.DeterminePositionStatus
import com.shreyashurakadli.engine.rules.position.common.PositionIsBase
import com.shreyashurakadli.engine.rules.position.common.PositionIsNormal
import com.shreyashurakadli.engine.rules.position.converter.RelativeToAbsolutePositionConverterImplementation
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.piece.PieceId
import com.shreyashurakadli.engine.state.piece.PieceStatus
import com.shreyashurakadli.engine.state.position.Position
import com.shreyashurakadli.engine.state.position.PositionStatus
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PieceRulesEnforcerTest {
    val positionRulesEnforcer = PositionRulesEnforcer(DeterminePositionStatus())
    val pieceRulesEnforcer = PieceRulesEnforcer(
        positionRulesEnforcer,
        converter = RelativeToAbsolutePositionConverterImplementation(
            positionIsNormalPosition = PositionIsNormal(),
            positionIsBase = PositionIsBase()
        )
    )

    @Test
    fun updatePiece_AtBase() {
        val previousPiece = Piece(
            id = PieceId(0),
            position = Position(
                part = 0,
                tile = -1,
                status = PositionStatus.Safe
            ),
            status = PieceStatus.InBase
        )
        val actualPiece = pieceRulesEnforcer.updatePiece(
            piece = previousPiece,
            diceValue = DiceValue(1),
            partCount = 4,
        )
        val expectedPiece = previousPiece
        assertEquals(expectedPiece, actualPiece)
    }

    @Test
    fun updatePiece_AtFinish() {
        val previousPiece = Piece(
            id = PieceId(0),
            position = Position(
                part = 0,
                tile = 17,
                status = PositionStatus.Safe
            ),
            status = PieceStatus.OnBoard
        )
        val actualPiece = pieceRulesEnforcer.updatePiece(
            piece = previousPiece,
            diceValue = DiceValue(1),
            partCount = 4,
        )
        val expectedPiece = previousPiece.copy(
            position = Position(
                part = 0,
                tile = 18,
                status = PositionStatus.Safe
            ),
            status = PieceStatus.Finished
        )
        assertEquals(expectedPiece, actualPiece)
    }

    @Test
    fun hasFinishedAllPieces_allAreFinishedPieces() {
        val input = listOf(
            Piece(id = PieceId(0), Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
            Piece(id = PieceId(1), Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
            Piece(id = PieceId(2), Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
            Piece(id = PieceId(3), Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
        )

        val actualResult = pieceRulesEnforcer.hasFinishedAllPieces(pieces = input)
        val expectedResult = true

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun hasFinishedAllPieces_someAreFinishedPieces() {
        val input = listOf(
            Piece(id = PieceId(0), Position(0, 17, PositionStatus.Safe), PieceStatus.OnBoard),
            Piece(id = PieceId(1), Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
            Piece(id = PieceId(2), Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
            Piece(id = PieceId(3), Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
        )

        val actualResult = pieceRulesEnforcer.hasFinishedAllPieces(pieces = input)
        val expectedResult = false

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun hasFinishedAllPieces_allAreOnBoardPieces() {
        val input = listOf(
            Piece(id = PieceId(0), Position(0, 17, PositionStatus.Safe), PieceStatus.OnBoard),
            Piece(id = PieceId(1), Position(0, 17, PositionStatus.Safe), PieceStatus.OnBoard),
            Piece(id = PieceId(2), Position(0, 17, PositionStatus.Safe), PieceStatus.OnBoard),
            Piece(id = PieceId(3), Position(0, 17, PositionStatus.Safe), PieceStatus.OnBoard),
        )

        val actualResult = pieceRulesEnforcer.hasFinishedAllPieces(pieces = input)
        val expectedResult = false

        assertEquals(expectedResult, actualResult)
    }
}