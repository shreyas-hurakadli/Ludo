package com.shreyashurakadli.engine.rules.position.converter

import com.shreyashurakadli.engine.state.position.Position

internal interface RelativeToAbsolutePositionConverter {
    fun calculateAbsolutePosition(position: Position, partCount: Int, playerPart: Int): Int
}