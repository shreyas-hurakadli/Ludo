package com.shreyashurakadli.engine.rules.position.common

import com.shreyashurakadli.engine.constants.PositionConstants
import com.shreyashurakadli.engine.state.position.Position

internal class PositionIsBase {
    operator fun invoke(position: Int): Boolean =
        position < 0

    operator fun invoke(position: Position): Boolean =
        position.tile == PositionConstants.BASE_POSITION
}