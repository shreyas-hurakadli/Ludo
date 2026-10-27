package com.shreyashurakadli.engine.engine

import com.shreyashurakadli.engine.GameEngineImplementation
import com.shreyashurakadli.engine.rules.common.DiceValue
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
import com.shreyashurakadli.engine.state.piece.PieceId
import com.shreyashurakadli.engine.state.player.PlayerId
import com.shreyashurakadli.engine.state.player.PlayerName
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
        val players = listOf(PlayerName("P1"), PlayerName("P2"))
        engine.init(players)
        assertEquals(2, engine.gameState.players.size)
        assertEquals(PlayerName("P1"), engine.gameState.players[0].name)
    }

    @Test
    fun `init throws exception for less than 2 players`() {
        val engine = GameEngineImplementation(gameRulesEnforcer)
        assertThrows<IllegalArgumentException> {
            engine.init(listOf(PlayerName("P1")))
        }
    }

    @Test
    fun `showAvailableOptions returns player id and piece ids`() {
        val engine = GameEngineImplementation(gameRulesEnforcer)
        engine.init(listOf(PlayerName("P1"), PlayerName("P2")))
        val (playerId, pieceIds) = engine.showAvailableOptions(DiceValue(6))
        assertEquals(PlayerId(0), playerId)
        assertEquals(4, pieceIds.size) // Initial 4 pieces in base
    }
}
