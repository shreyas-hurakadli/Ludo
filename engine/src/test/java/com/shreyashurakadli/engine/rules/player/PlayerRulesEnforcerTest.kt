package com.shreyashurakadli.engine.rules.player

import com.shreyashurakadli.engine.rules.piece.PieceRulesEnforcer
import com.shreyashurakadli.engine.rules.piece.position.PositionRulesEnforcer
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.piece.PieceStatus
import com.shreyashurakadli.engine.state.piece.position.Position
import com.shreyashurakadli.engine.state.piece.position.PositionStatus
import com.shreyashurakadli.engine.state.player.Player
import com.shreyashurakadli.engine.state.player.PlayerStatus
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PlayerRulesEnforcerTest {
    private val positionRulesEnforcer = PositionRulesEnforcer()
    private val pieceRulesEnforcer =
        PieceRulesEnforcer(positionRulesEnforcer = positionRulesEnforcer)
    private val playerRulesEnforcer: PlayerRulesEnforcer =
        PlayerRulesEnforcer(pieceRulesEnforcer = pieceRulesEnforcer)

    @Test
    fun updatePlayer_playerStatusChange() {
        val player = Player(
            id = 0,
            name = "Shreyas",
            status = PlayerStatus.InProgress,
            pieces = listOf(
                Piece(Position(0, 17, PositionStatus.Safe), PieceStatus.OnBoard),
                Piece(Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                Piece(Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                Piece(Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
            )
        )

        val actualResult = playerRulesEnforcer.updatePlayer(player, diceValue = 1, pieceIdx = 0)

        val expectedResult = player.copy(
            pieces = listOf(
                Piece(Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                Piece(Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                Piece(Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                Piece(Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
            ),
            status = PlayerStatus.Won
        )

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun updatePlayerPieces() {
        val pieces = listOf(
            Piece(Position(0, 17, PositionStatus.Safe), PieceStatus.OnBoard),
            Piece(Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
            Piece(Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
            Piece(Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
        )

        val actualResult = playerRulesEnforcer.updatePlayerPieces(0, pieces, 1)
        val expectedResult = listOf(
            Piece(Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
            Piece(Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
            Piece(Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
            Piece(Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
        )

        assertEquals(expectedResult, actualResult)
    }
}