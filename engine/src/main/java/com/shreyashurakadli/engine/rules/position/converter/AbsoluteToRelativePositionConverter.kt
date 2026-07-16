package com.shreyashurakadli.engine.rules.position.converter

import com.shreyashurakadli.engine.state.position.Position

internal interface AbsoluteToRelativePositionConverter {
    fun calculateRelativePosition(position: Int, partCount: Int): Position
}