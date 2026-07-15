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
                Piece(id = 0, Position(0, 17, PositionStatus.Safe), PieceStatus.OnBoard),
                Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
            )
        )

        val actualResult = playerRulesEnforcer.updatePlayer(
            player = player,
            diceValue = 1,
            piece = player.pieces[0]
        )

        val expectedResult = player.copy(
            pieces = listOf(
                Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
            ),
            status = PlayerStatus.Won
        )

        assertEquals(expectedResult, actualResult)
    }
}