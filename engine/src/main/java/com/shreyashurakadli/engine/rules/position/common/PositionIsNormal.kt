package com.shreyashurakadli.engine.rules.position.common

import com.shreyashurakadli.engine.constants.PositionConstants
import com.shreyashurakadli.engine.state.position.Position

internal class PositionIsNormal {
    operator fun invoke(absPos: Int, partCount: Int): Boolean =
        absPos < partCount * PositionConstants.TOTAL_POSITIONS_IN_PART

    operator fun invoke(position: Position): Boolean =
        when (position.tile) {
            in PositionConstants.HOME_POSITIONS -> false
            PositionConstants.FINAL_POSITION -> false
            PositionConstants.BASE_POSITION -> false
            else -> true
        }
}