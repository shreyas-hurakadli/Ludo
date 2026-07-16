package com.shreyashurakadli.engine.rules.position

import com.shreyashurakadli.engine.constants.PositionConstants
import com.shreyashurakadli.engine.state.position.Position
import com.shreyashurakadli.engine.state.position.PositionStatus

internal class PositionRulesEnforcer : PositionRules {
    override fun updatePosition(state: Position, diceValue: Int, quadrantCount: Int): Position {
        val newTile = handleTile(
            part = state.part,
            tile = state.tile,
            diceValue = diceValue
        )

        val newPart = handlePart(
            part = state.part,
            tile = state.tile,
            diceValue = diceValue,
            quadrantCount = quadrantCount
        )

        val newStatus = determineStatus(tile = newTile)

        return Position(
            part = newPart,
            tile = newTile,
            status = newStatus
        )
    }

    private fun handleTile(part: Int, tile: Int, diceValue: Int): Int {
        // Handle movement from base position to first position in board
        if (isAtBasePosition(tile)) {
            return if (shouldEnterBoardFromBase(diceValue = diceValue)) {
                PositionConstants.START_POSITION
            } else {
                PositionConstants.BASE_POSITION
            }
        }

        if (isAtFinalPosition(tile)) {
            return PositionConstants.FINAL_POSITION
        }

        if (isEnteringHomePositions(part, tile, diceValue)) {
            // Determine the number of jumps to reach first position on home path
            val remainingDiceValue =
                diceValue - (PositionConstants.LAST_NON_HOME_POSITION - tile + 1)
            val finalTile = PositionConstants.HOME_POSITION_0 + remainingDiceValue
            return finalTile
        }

        if (isMovingOnHomePath(tile)) {
            if (diceValueExceedsAvailableOptions(tile, diceValue)) {
                return tile
            }
            return tile + diceValue
        }

        // Handles position change in both same and different part
        return (tile + diceValue) % PositionConstants.TOTAL_POSITIONS_IN_PART
    }

    override fun isAtBasePosition(tile: Int): Boolean =
        tile == PositionConstants.BASE_POSITION

    override fun isAtFinalPosition(tile: Int): Boolean =
        tile == PositionConstants.FINAL_POSITION

    private fun shouldEnterBoardFromBase(diceValue: Int): Boolean =
        diceValue == 6

    private fun isEnteringHomePositions(part: Int, tile: Int, diceValue: Int): Boolean =
        part == PositionConstants.START_PART && tile <= PositionConstants.LAST_NON_HOME_POSITION &&
                tile + diceValue > PositionConstants.LAST_NON_HOME_POSITION

    private fun isMovingOnHomePath(tile: Int): Boolean =
        tile in PositionConstants.HOME_POSITIONS

    private fun diceValueExceedsAvailableOptions(tile: Int, diceValue: Int): Boolean =
        tile + diceValue > PositionConstants.FINAL_POSITION

    private fun handlePart(part: Int, tile: Int, diceValue: Int, quadrantCount: Int): Int =
        if (isEnteringNewPart(tile, diceValue)) {
            (part + 1) % quadrantCount
        } else {
            part
        }

    private fun isEnteringNewPart(tile: Int, diceValue: Int): Boolean =
        tile !in PositionConstants.HOME_POSITIONS &&
                tile + diceValue >= PositionConstants.TOTAL_POSITIONS_IN_PART

    private fun determineStatus(tile: Int): PositionStatus =
        when (tile) {
            in PositionConstants.SAFE_POSITIONS -> PositionStatus.Safe
            in PositionConstants.HOME_POSITIONS -> PositionStatus.Safe
            else -> PositionStatus.Unsafe
        }
}