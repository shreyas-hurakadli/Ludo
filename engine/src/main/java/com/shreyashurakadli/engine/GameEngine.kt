package com.shreyashurakadli.engine

import com.shreyashurakadli.engine.rules.common.DiceValue
import com.shreyashurakadli.engine.rules.game.GameRules
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
import com.shreyashurakadli.engine.state.piece.PieceId
import com.shreyashurakadli.engine.state.player.PlayerId
import com.shreyashurakadli.engine.state.player.PlayerName

interface GameEngine {
    /**
     * The current state of the game.
     * Observe this to read players, turn, and game status.
     */
    val gameState: Game

    /**
     * Initializes the game.
     * Execute this before starting the game.
     * @param players List of players with their usernames
     */
    fun init(players: List<PlayerName>)

    /**
     * Shows available options for the given dice value.
     * Returns a pair of player id and piece id(s).
     * @param diceValue
     */
    fun showAvailableOptions(diceValue: DiceValue): Pair<PlayerId, List<PieceId>>

    /**
     * Moves the piece.
     * @param pieceId
     * @param diceValue
     */
    fun movePiece(pieceId: PieceId, diceValue: DiceValue)

    companion object {
        /**
         * Creates an instance of GameEngine.
         * Wires the internal game rules dependency graph.
         */
        fun create(): GameEngine =
            GameEngineImplementation(createGameRules())

        private fun createGameRules(): GameRules {
            val positionRulesEnforcer = PositionRulesEnforcer(
                determinePositionStatus = DeterminePositionStatus()
            )

            val pieceRulesEnforcer = PieceRulesEnforcer(
                positionRulesEnforcer = positionRulesEnforcer,
                converter = RelativeToAbsolutePositionConverterImplementation(
                    positionIsNormalPosition = PositionIsNormal(),
                    positionIsBase = PositionIsBase()
                )
            )

            val playerRulesEnforcer = PlayerRulesEnforcer(
                pieceRulesEnforcer = pieceRulesEnforcer
            )

            return GameRulesEnforcer(
                playerRulesEnforcer = playerRulesEnforcer,
                pieceHasFinishedStatus = PieceHasFinishedStatus(),
                getUpdatedPiece = GetUpdatedPiece()
            )
        }
    }
}