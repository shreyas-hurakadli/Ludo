package com.shreyashurakadli.engine.rules.game

import com.shreyashurakadli.engine.rules.piece.PieceRulesEnforcer
import com.shreyashurakadli.engine.rules.piece.position.PositionRulesEnforcer
import com.shreyashurakadli.engine.rules.player.PlayerRulesEnforcer
import com.shreyashurakadli.engine.state.game.Game
import com.shreyashurakadli.engine.state.game.GameStatus
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.piece.PieceStatus
import com.shreyashurakadli.engine.state.piece.position.Position
import com.shreyashurakadli.engine.state.piece.position.PositionStatus
import com.shreyashurakadli.engine.state.player.Player
import com.shreyashurakadli.engine.state.player.PlayerStatus
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GameRulesEnforcerTest {
    private val positionRulesEnforcer = PositionRulesEnforcer()
    private val pieceRulesEnforcer = PieceRulesEnforcer(positionRulesEnforcer)
    private val playerRulesEnforcer = PlayerRulesEnforcer(pieceRulesEnforcer)
    private val gameRulesEnforcer = GameRulesEnforcer(playerRulesEnforcer)

    @Test
    fun updateGameState_statusChange() {
        val players = listOf(
            Player(
                id = 0,
                name = "Player 1",
                status = PlayerStatus.InProgress,
                pieces = listOf(
                    Piece(id = 0,Position(0, 17, PositionStatus.Safe), PieceStatus.OnBoard),
                    Piece(id = 1,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 1,
                name = "Player 2",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 2,
                name = "Player 3",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 3,
                name = "Player 4",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
        )
        val newPlayers = listOf(
            Player(
                id = 0,
                name = "Player 1",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 1,
                name = "Player 2",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 2,
                name = "Player 3",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 3,
                name = "Player 4",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3,Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
        )

        val input = Game(
            players = players,
            currentTurnPlayerIdx = 0,
            status = GameStatus.InProgress
        )
        val actualResult = gameRulesEnforcer.updateGameState(
            gameState = input,
            diceValue = 1,
            piece = players[0].pieces[0],
        )
        val expectedResult = Game(
            players = newPlayers,
            currentTurnPlayerIdx = 0,
            status = GameStatus.Finished
        )

        assertEquals(expectedResult, actualResult)
    }
}