package com.shreyashurakadli.engine.rules.position.converter

import com.shreyashurakadli.engine.state.position.Part
import com.shreyashurakadli.engine.state.position.Position

internal interface RelativeToAbsolutePositionConverter {
    fun calculateAbsolutePosition(position: Position, partCount: Int, playerPart: Part): Int
}
