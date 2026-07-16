package com.shreyashurakadli.engine.rules.position.converter

import com.shreyashurakadli.engine.state.position.Position

internal interface RelativeToAbsolutePositionConverter {
    fun calculateAbsolutePath(position: Position, partCount: Int): Int
}