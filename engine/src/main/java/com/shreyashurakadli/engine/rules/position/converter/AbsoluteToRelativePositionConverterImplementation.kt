package com.shreyashurakadli.engine.rules.position.converter

import com.shreyashurakadli.engine.constants.PositionConstants
import com.shreyashurakadli.engine.rules.position.common.DeterminePositionStatus
import com.shreyashurakadli.engine.rules.position.common.PositionIsBase
import com.shreyashurakadli.engine.rules.position.common.PositionIsNormal
import com.shreyashurakadli.engine.state.position.Position
import com.shreyashurakadli.engine.state.position.PositionStatus
import kotlin.math.abs

internal class AbsoluteToRelativePositionConverterImplementation(
    private val positionIsNormalPosition: PositionIsNormal,
    private val determinePositionStatus: DeterminePositionStatus,
    private val positionIsBase: PositionIsBase
) : AbsoluteToRelativePositionConverter {
    override fun calculateRelativePosition(position: Int, partCount: Int): Position {
        val relativePosition =
            if (positionIsNormalPosition(position, partCount)) {
                val tile = position % PositionConstants.TOTAL_POSITIONS_IN_PART
                val part = (position - tile) / PositionConstants.TOTAL_POSITIONS_IN_PART

                Position(
                    part = part,
                    tile = tile,
                    status = determinePositionStatus(tile)
                )
            } else if (positionIsBase(position)) {
                Position(
                    part = PositionConstants.START_PART,
                    tile = abs(n = position) * PositionConstants.BASE_POSITION,
                    status = PositionStatus.Safe
                )
            } else {
                val offset = position - partCount * PositionConstants.TOTAL_POSITIONS_IN_PART

                // Home Positions + Final Position
                val tile =
                    offset % (PositionConstants.HOME_POSITIONS.size + 1) + PositionConstants.HOME_POSITION_0
                val part = offset / (PositionConstants.HOME_POSITIONS.size + 1)

                Position(
                    part = part,
                    tile = tile,
                    status = PositionStatus.Safe
                )
            }

        return relativePosition
    }
}