package com.shreyashurakadli.engine.rules.position.converter

import com.shreyashurakadli.engine.constants.PositionConstants
import com.shreyashurakadli.engine.state.position.Position

internal class RelativeToAbsolutePositionConverterImplementation :
    RelativeToAbsolutePositionConverter {
    override fun calculateAbsolutePosition(position: Position, partCount: Int): Int {
        val firstHomeAbsolutePosition = partCount * PositionConstants.TOTAL_POSITIONS_IN_PART

        val partOffset = position.part * PositionConstants.TOTAL_POSITIONS_IN_PART

        // provisionalPosition = partOffset + (position.part * PositionConstants.TOTAL_POSITIONS_IN_PART)
        //                       + position.tile
        // The part in the bracket is the exact definition of partOffset, hence it is multiplied
        // by 2
        // For normal position: absolutePosition == provisionalPosition
        val provisionalPosition = 2 * partOffset + position.tile

        // Home position conversion and normal position conversion are handled separately
        val absolutePosition = if (positionIsNormalPosition(provisionalPosition, firstHomeAbsolutePosition)) {
            provisionalPosition
        } else {
            val noOfHomePositionsInPart = PositionConstants.HOME_POSITIONS.size
            val homePositionOffset = noOfHomePositionsInPart * position.part
            val relativeHomePositionOffset = position.tile - PositionConstants.HOME_POSITION_0
            firstHomeAbsolutePosition + homePositionOffset + relativeHomePositionOffset
        }

        return absolutePosition
    }

    override fun positionIsNormalPosition(absPos: Int, firstHomeAbsPos: Int): Boolean =
        absPos < firstHomeAbsPos
}