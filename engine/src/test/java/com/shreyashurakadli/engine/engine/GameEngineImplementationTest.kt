package com.shreyashurakadli.engine.engine

import com.shreyashurakadli.engine.GameEngineImplementation
import com.shreyashurakadli.engine.rules.game.GameRulesEnforcer
import com.shreyashurakadli.engine.rules.piece.PieceRulesEnforcer
import com.shreyashurakadli.engine.rules.piece.common.GetUpdatedPiece
import com.shreyashurakadli.engine.rules.piece.common.PieceHasFinishedStatus
import com.shreyashurakadli.engine.rules.player.PlayerRulesEnforcer
import com.shreyashurakadli.engine.rules.position.PositionRulesEnforcer
import com.shreyashurakadli.engine.rules.position.common.DeterminePositionStatus
import com.shreyashurakadli.engine.rules.position.common.PositionIsBase
import com.shreyashurakadli.engine.rules.position.common.PositionIsNormal
import com.shreyashurakadli.engine.rules.position.converter.RelativeToAbsolutePositionConverterImplementation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GameEngineImplementationTest {

    private val positionRulesEnforcer = PositionRulesEnforcer(
        determinePositionStatus = DeterminePositionStatus()
    )
    private val pieceRulesEnforcer = PieceRulesEnforcer(
        positionRulesEnforcer,
        converter = RelativeToAbsolutePositionConverterImplementation(
            positionIsNormalPosition = PositionIsNormal(),
            positionIsBase = PositionIsBase()
        )
    )
    private val playerRulesEnforcer = PlayerRulesEnforcer(
        pieceRulesEnforcer,
    )
    private val gameRulesEnforcer = GameRulesEnforcer(
        playerRulesEnforcer,
        pieceHasFinishedStatus = PieceHasFinishedStatus(),
        getUpdatedPiece = GetUpdatedPiece(),
    )

    @Test
    fun `init initializes game state with players`() {
        val engine = GameEngineImplementation(gameRulesEnforcer)
        val players = listOf("P1", "P2")
        engine.init(2, players)
        assertEquals(2, engine.gameState.players.size)
        assertEquals("P1", engine.gameState.players[0].name)
    }

    @Test
    fun `init throws exception for less than 2 players`() {
        val engine = GameEngineImplementation(gameRulesEnforcer)
        assertThrows<IllegalArgumentException> {
            engine.init(1, listOf("P1"))
        }
    }

    @Test
    fun `showAvailableOptions returns player id and piece ids`() {
        val engine = GameEngineImplementation(gameRulesEnforcer)
        engine.init(2, listOf("P1", "P2"))
        val (playerId, pieceIds) = engine.showAvailableOptions(6)
        assertEquals(0, playerId)
        assertEquals(4, pieceIds.size) // Initial 4 pieces in base
    }
}
