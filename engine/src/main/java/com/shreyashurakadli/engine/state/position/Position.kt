package com.shreyashurakadli.engine.state.position

import com.shreyashurakadli.engine.constants.PositionConstants

@ConsistentCopyVisibility
data class Position internal constructor(
    val part: Int,
    val tile: Int,
    val status: PositionStatus
) {
    companion object {
        fun initialPosition(): Position =
            Position(
                part = PositionConstants.BASE_POSITION,
                tile = PositionConstants.START_PART,
                status = PositionStatus.Safe
            )
    }
}