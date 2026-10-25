package com.shreyashurakadli.engine

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

interface GameEngine {
    /**
     * Initializes the game.
     * Execute this before starting the game.
     * @param noOfPlayers
     * @param players List of players with their usernames
     */
    fun init(noOfPlayers: Int, players: List<String>)

    /**
     * Shows available options for the given dice value.
     * Returns a pair of player id and piece id(s).
     * @param diceValue
     */
    fun showAvailableOptions(diceValue: Int): Pair<Int, List<Int>>

    /**
     * Moves the piece.
     * @param pieceId
     * @param diceValue
     */
    fun movePiece(pieceId: Int, diceValue: Int)

    /**
     * Tells if the game is completed.
     */
    fun isGameCompleted(): Boolean

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