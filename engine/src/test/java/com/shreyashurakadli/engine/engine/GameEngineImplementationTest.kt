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
import com.shreyashurakadli.engine.state.game.Game
import com.shreyashurakadli.engine.state.game.GameStatus
import com.shreyashurakadli.engine.state.piece.PieceId
import com.shreyashurakadli.engine.state.piece.PieceStatus
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

    @Test
    fun `movePiece throws when game is not initialized`() {
        val engine = GameEngineImplementation(gameRulesEnforcer)
        assertThrows<IllegalStateException> {
            engine.movePiece(PieceId(0), DiceValue(1))
        }
    }

    @Test
    fun `showAvailableOptions throws when game is not initialized`() {
        val engine = GameEngineImplementation(gameRulesEnforcer)
        assertThrows<IllegalStateException> {
            engine.showAvailableOptions(DiceValue(1))
        }
    }

    @Test
    fun `skipCurrentTurn throws when game is not initialized`() {
        val engine = GameEngineImplementation(gameRulesEnforcer)
        assertThrows<IllegalStateException> {
            engine.skipCurrentTurn()
        }
    }

    @Test
    fun `skipCurrentTurn advances turn and wraps around`() {
        val engine = GameEngineImplementation(gameRulesEnforcer)
        engine.init(listOf(PlayerName("P1"), PlayerName("P2")))

        engine.skipCurrentTurn()
        assertEquals(1, engine.gameState.currentTurnPlayerIdx)

        engine.skipCurrentTurn()
        assertEquals(0, engine.gameState.currentTurnPlayerIdx) // Wrapped around
    }

    @Test
    fun `showAvailableOptions throws when game is completed`() {
        val engine = GameEngineImplementation(gameRulesEnforcer)
        engine.init(listOf(PlayerName("P1"), PlayerName("P2")))

        winWithPlayer0(engine)

        assertEquals(GameStatus.Finished, engine.gameState.status)
        assertThrows<IllegalStateException> {
            engine.showAvailableOptions(DiceValue(1))
        }
    }

    @Test
    fun `skipCurrentTurn does nothing when game is completed`() {
        val engine = GameEngineImplementation(gameRulesEnforcer)
        engine.init(listOf(PlayerName("P1"), PlayerName("P2")))

        winWithPlayer0(engine)
        val turnAfterWin = engine.gameState.currentTurnPlayerIdx

        engine.skipCurrentTurn()
        assertEquals(turnAfterWin, engine.gameState.currentTurnPlayerIdx)
    }

    @Test
    fun `movePiece does nothing when game is completed`() {
        val engine = GameEngineImplementation(gameRulesEnforcer)
        engine.init(listOf(PlayerName("P1"), PlayerName("P2")))

        val stateAfterWin = winWithPlayer0(engine)

        engine.movePiece(PieceId(0), DiceValue(1))
        assertEquals(stateAfterWin, engine.gameState)
    }

    /**
     * Plays P1 to victory in a two-player game.
     * Dice 6 leaves base and grants extra turns; dice 1 walks the piece home.
     * Whenever the turn passes to P2, skipCurrentTurn hands it back to P1.
     * Returns the game state after P1 wins.
     */
    private fun winWithPlayer0(engine: GameEngineImplementation): Game {
        for (pieceValue in 0..3) {
            val pieceId = PieceId(pieceValue)
            engine.movePiece(pieceId, DiceValue(6))
            while (engine.gameState.players[0].pieces[pieceValue].status != PieceStatus.Finished) {
                if (engine.gameState.currentTurnPlayerIdx != 0) {
                    engine.skipCurrentTurn()
                }
                engine.movePiece(pieceId, DiceValue(1))
            }
        }
        return engine.gameState
    }
}
