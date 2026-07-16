package com.shreyashurakadli.engine.rules.position.converter

import com.shreyashurakadli.engine.constants.PositionConstants
import com.shreyashurakadli.engine.state.position.PositionStatus

internal class DeterminePositionStatus {
    operator fun invoke(tile: Int): PositionStatus =
        when (tile) {
            in PositionConstants.SAFE_POSITIONS -> PositionStatus.Safe
            in PositionConstants.HOME_POSITIONS -> PositionStatus.Safe
            else -> PositionStatus.Unsafe
        }
}