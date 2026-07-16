package com.shreyashurakadli.engine.rules.position.converter

import com.shreyashurakadli.engine.constants.PositionConstants
import com.shreyashurakadli.engine.state.position.Position
import com.shreyashurakadli.engine.state.position.PositionStatus

internal class AbsoluteToRelativePositionConverterImplementation(
    private val positionIsNormalPosition: PositionIsNormal,
    private val determinePositionStatus: DeterminePositionStatus
) : AbsoluteToRelativePositionConverter {
    override fun calculateRelativePosition(position: Int, partCount: Int): Position {
        val relativePosition = if (positionIsNormalPosition(position, partCount)) {
            val tile = position % PositionConstants.TOTAL_POSITIONS_IN_PART
            val part = (position - tile) / PositionConstants.TOTAL_POSITIONS_IN_PART

            Position(
                part = part,
                tile = tile,
                status = determinePositionStatus(tile)
            )
        } else {
            val offset = position - partCount * PositionConstants.TOTAL_POSITIONS_IN_PART

            val tile = offset % PositionConstants.HOME_POSITIONS.size + PositionConstants.HOME_POSITION_0
            val part = offset / PositionConstants.HOME_POSITIONS.size

            Position(
                part = part,
                tile = tile,
                status = PositionStatus.Safe
            )
        }

        return relativePosition
    }
}