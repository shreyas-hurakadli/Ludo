package com.shreyashurakadli.engine.rules.position.converter

import com.shreyashurakadli.engine.constants.PositionConstants
import com.shreyashurakadli.engine.rules.position.common.PositionIsBase
import com.shreyashurakadli.engine.rules.position.common.PositionIsNormal
import com.shreyashurakadli.engine.state.position.Position

internal class RelativeToAbsolutePositionConverterImplementation(
    private val positionIsNormalPosition: PositionIsNormal,
    private val positionIsBase: PositionIsBase
) : RelativeToAbsolutePositionConverter {
    override fun calculateAbsolutePosition(
        position: Position,
        partCount: Int,
        playerPart: Int
    ): Int {
        val absolutePosition =
            if (positionIsNormalPosition(position)) {
                val partOffset = position.part * PositionConstants.TOTAL_POSITIONS_IN_PART
                val playerOffset = playerPart * PositionConstants.TOTAL_POSITIONS_IN_PART
                playerOffset + partOffset + position.tile
            } else if (positionIsBase(position)) {
                position.tile * (playerPart + 1)
            } else {
                val noOfHomePositionsInPart = PositionConstants.HOME_POSITIONS.size
                val homePositionOffset = noOfHomePositionsInPart * position.part
                val relativeHomePositionOffset = position.tile - PositionConstants.HOME_POSITION_0
                val playerOffset = playerPart * (PositionConstants.HOME_POSITIONS.size + 1)
                playerOffset + partCount * PositionConstants.TOTAL_POSITIONS_IN_PART + homePositionOffset + relativeHomePositionOffset
            }

        return absolutePosition
    }
}