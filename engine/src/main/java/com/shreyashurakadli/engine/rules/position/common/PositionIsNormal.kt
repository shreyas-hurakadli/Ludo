package com.shreyashurakadli.engine.rules.position.common

import com.shreyashurakadli.engine.constants.PositionConstants

internal class PositionIsNormal {
    operator fun invoke(absPos: Int, partCount: Int): Boolean =
        absPos < partCount * PositionConstants.TOTAL_POSITIONS_IN_PART
}