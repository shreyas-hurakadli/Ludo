package com.shreyashurakadli.engine.rules.game

import com.shreyashurakadli.engine.rules.piece.PieceRulesEnforcer
import com.shreyashurakadli.engine.rules.piece.common.GetUpdatedPiece
import com.shreyashurakadli.engine.rules.piece.common.HasCapturedPiece
import com.shreyashurakadli.engine.rules.piece.common.PieceHasFinishedStatus
import com.shreyashurakadli.engine.rules.player.PlayerRulesEnforcer
import com.shreyashurakadli.engine.rules.position.PositionRulesEnforcer
import com.shreyashurakadli.engine.rules.position.common.DeterminePositionStatus
import com.shreyashurakadli.engine.rules.position.common.GetBasePositionPiece
import com.shreyashurakadli.engine.rules.position.common.PositionIsBase
import com.shreyashurakadli.engine.rules.position.common.PositionIsNormal
import com.shreyashurakadli.engine.rules.position.converter.RelativeToAbsolutePositionConverterImplementation
import com.shreyashurakadli.engine.state.game.Game
import com.shreyashurakadli.engine.state.game.GameStatus
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.piece.PieceStatus
import com.shreyashurakadli.engine.state.player.Player
import com.shreyashurakadli.engine.state.player.PlayerStatus
import com.shreyashurakadli.engine.state.position.Position
import com.shreyashurakadli.engine.state.position.PositionStatus
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GameRulesEnforcerTest {
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
    fun updateGameState_statusChange() {
        val players = listOf(
            Player(
                id = 0,
                name = "Player 1",
                status = PlayerStatus.InProgress,
                pieces = listOf(
                    Piece(id = 0, Position(0, 17, PositionStatus.Safe), PieceStatus.OnBoard),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 1,
                name = "Player 2",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 2,
                name = "Player 3",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 3,
                name = "Player 4",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
        )
        val newPlayers = listOf(
            Player(
                id = 0,
                name = "Player 1",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 1,
                name = "Player 2",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 2,
                name = "Player 3",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 3,
                name = "Player 4",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
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
            partCount = 4,
        )
        val expectedResult = Game(
            players = newPlayers,
            currentTurnPlayerIdx = -1,
            status = GameStatus.Finished
        )

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun updateGameState_playerTurnChange() {
        val players = listOf(
            Player(
                id = 0,
                name = "Player 1",
                status = PlayerStatus.InProgress,
                pieces = listOf(
                    Piece(id = 0, Position(0, 16, PositionStatus.Safe), PieceStatus.OnBoard),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 1,
                name = "Player 2",
                status = PlayerStatus.InProgress,
                pieces = listOf(
                    Piece(id = 0, Position(0, 17, PositionStatus.Safe), PieceStatus.OnBoard),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 2,
                name = "Player 3",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 3,
                name = "Player 4",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
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
            partCount = 4
        )

        val newPlayers = players.map { player ->
            if (player.id == 0) {
                player.copy(
                    pieces = player.pieces.map {
                        if (it.id == 0) {
                            it.copy(position = Position(0, 17, PositionStatus.Safe))
                        } else {
                            it
                        }
                    },
                    status = PlayerStatus.InProgress
                )
            } else {
                player
            }
        }

        val expectedResult = Game(
            players = newPlayers,
            currentTurnPlayerIdx = 1,
            status = GameStatus.InProgress
        )

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun updateGameState_pieceCapture() {
        val players = listOf(
            Player(
                id = 0,
                name = "Player 1",
                status = PlayerStatus.InProgress,
                pieces = listOf(
                    Piece(id = 0, Position(2, 8, PositionStatus.Safe), PieceStatus.OnBoard),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 1,
                name = "Player 2",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 2,
                name = "Player 3",
                status = PlayerStatus.InProgress,
                pieces = listOf(
                    Piece(id = 0, Position(0, 9, PositionStatus.Unsafe), PieceStatus.OnBoard),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            )
        )

        val input = Game(
            players = players,
            currentTurnPlayerIdx = 0,
            status = GameStatus.InProgress
        )

        // Player 0 at (2, 8) moves 1 step to (2, 9).
        // Absolute Pos Player 0: (0 * 13) + (2 * 13) + 9 = 35
        // Absolute Pos Player 2: (2 * 13) + (0 * 13) + 9 = 35
        // Tile 9 is Unsafe, so capture should occur.

        val actualResult = gameRulesEnforcer.updateGameState(
            gameState = input,
            diceValue = 1,
            piece = players[0].pieces[0],
            partCount = 4,
        )

        val expectedPlayers = listOf(
            Player(
                id = 0,
                name = "Player 1",
                status = PlayerStatus.InProgress,
                pieces = listOf(
                    Piece(id = 0, Position(2, 9, PositionStatus.Unsafe), PieceStatus.OnBoard),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 1,
                name = "Player 2",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 2,
                name = "Player 3",
                status = PlayerStatus.InProgress,
                pieces = listOf(
                    Piece(id = 0, Position(0, -1, PositionStatus.Safe), PieceStatus.InBase),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            )
        )

        val expectedResult = Game(
            players = expectedPlayers,
            currentTurnPlayerIdx = 0, 
            status = GameStatus.InProgress
        )

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun updateGameState_noCaptureOnSafePosition() {
        val players = listOf(
            Player(
                id = 0,
                name = "Player 1",
                status = PlayerStatus.InProgress,
                pieces = listOf(
                    Piece(id = 0, Position(2, 7, PositionStatus.Unsafe), PieceStatus.OnBoard),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 1,
                name = "Player 2",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 2,
                name = "Player 3",
                status = PlayerStatus.InProgress,
                pieces = listOf(
                    Piece(id = 0, Position(0, 8, PositionStatus.Safe), PieceStatus.OnBoard),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            )
        )

        val input = Game(
            players = players,
            currentTurnPlayerIdx = 0,
            status = GameStatus.InProgress
        )

        // Player 0 at (2, 7) moves 1 step to (2, 8).
        // Absolute Pos Player 0: (0 * 13) + (2 * 13) + 8 = 34
        // Absolute Pos Player 2: (2 * 13) + (0 * 13) + 8 = 34
        // Tile 8 is Safe (START_POSITION), so no capture should occur.

        val actualResult = gameRulesEnforcer.updateGameState(
            gameState = input,
            diceValue = 1,
            piece = players[0].pieces[0],
            partCount = 4,
        )

        val expectedPlayers = listOf(
            Player(
                id = 0,
                name = "Player 1",
                status = PlayerStatus.InProgress,
                pieces = listOf(
                    Piece(id = 0, Position(2, 8, PositionStatus.Safe), PieceStatus.OnBoard),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 1,
                name = "Player 2",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 2,
                name = "Player 3",
                status = PlayerStatus.InProgress,
                pieces = listOf(
                    Piece(id = 0, Position(0, 8, PositionStatus.Safe), PieceStatus.OnBoard),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            )
        )

        val expectedResult = Game(
            players = expectedPlayers,
            currentTurnPlayerIdx = 2, // Skip Player 1 (Won), Turn passes to Player 2
            status = GameStatus.InProgress
        )

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun updateGameState_rollSix_allPiecesInBase() {
        val players = listOf(
            Player(
                id = 0,
                name = "Player 1",
                status = PlayerStatus.InProgress,
                pieces = listOf(
                    Piece(id = 0, Position(0, -1, PositionStatus.Safe), PieceStatus.InBase),
                    Piece(id = 1, Position(0, -1, PositionStatus.Safe), PieceStatus.InBase),
                    Piece(id = 2, Position(0, -1, PositionStatus.Safe), PieceStatus.InBase),
                    Piece(id = 3, Position(0, -1, PositionStatus.Safe), PieceStatus.InBase),
                )
            ),
            Player(
                id = 1,
                name = "Player 2",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 2,
                name = "Player 3",
                status = PlayerStatus.InProgress,
                pieces = listOf(
                    Piece(id = 0, Position(0, -1, PositionStatus.Safe), PieceStatus.InBase),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            )
        )

        val input = Game(
            players = players,
            currentTurnPlayerIdx = 0,
            status = GameStatus.InProgress
        )

        // Rolling a 6 should move piece 0 to START_POSITION (tile 8)
        val actualResult = gameRulesEnforcer.updateGameState(
            gameState = input,
            diceValue = 6,
            piece = players[0].pieces[0],
            partCount = 4,
        )

        val expectedPlayers = listOf(
            Player(
                id = 0,
                name = "Player 1",
                status = PlayerStatus.InProgress,
                pieces = listOf(
                    Piece(id = 0, Position(0, 8, PositionStatus.Safe), PieceStatus.OnBoard),
                    Piece(id = 1, Position(0, -1, PositionStatus.Safe), PieceStatus.InBase),
                    Piece(id = 2, Position(0, -1, PositionStatus.Safe), PieceStatus.InBase),
                    Piece(id = 3, Position(0, -1, PositionStatus.Safe), PieceStatus.InBase),
                )
            ),
            Player(
                id = 1,
                name = "Player 2",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 2,
                name = "Player 3",
                status = PlayerStatus.InProgress,
                pieces = listOf(
                    Piece(id = 0, Position(0, -1, PositionStatus.Safe), PieceStatus.InBase),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            )
        )

        val expectedResult = Game(
            players = expectedPlayers,
            currentTurnPlayerIdx = 0, // Extra turn for rolling a 6
            status = GameStatus.InProgress
        )

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun updateGameState_finishLastPiece_withSix() {
        val players = listOf(
            Player(
                id = 0,
                name = "Player 1",
                status = PlayerStatus.InProgress,
                pieces = listOf(
                    Piece(id = 0, Position(0, 6, PositionStatus.Unsafe), PieceStatus.OnBoard),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 1,
                name = "Player 2",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 2,
                name = "Player 3",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 3,
                name = "Player 4",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            )
        )

        val input = Game(
            players = players,
            currentTurnPlayerIdx = 0,
            status = GameStatus.InProgress
        )

        // Player 0 at (0, 12) moves 6 steps. 
        // 12 -> 6 (last non-home) -> 13, 14, 15, 16, 17, 18.
        // It should reach tile 18 and finish the game.

        val actualResult = gameRulesEnforcer.updateGameState(
            gameState = input,
            diceValue = 6,
            piece = players[0].pieces[0],
            partCount = 4,
        )

        val expectedPlayers = listOf(
            Player(
                id = 0,
                name = "Player 1",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 1,
                name = "Player 2",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 2,
                name = "Player 3",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            ),
            Player(
                id = 3,
                name = "Player 4",
                status = PlayerStatus.Won,
                pieces = listOf(
                    Piece(id = 0, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 1, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 2, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                    Piece(id = 3, Position(0, 18, PositionStatus.Safe), PieceStatus.Finished),
                )
            )
        )

        val expectedResult = Game(
            players = expectedPlayers,
            currentTurnPlayerIdx = -1, // Game Finished
            status = GameStatus.Finished
        )

        assertEquals(expectedResult, actualResult)
    }
}
