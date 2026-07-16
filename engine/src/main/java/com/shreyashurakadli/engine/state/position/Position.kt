package com.shreyashurakadli.engine.state.position

internal data class Position(
    val part: Int,
    val tile: Int,
    val status: PositionStatus
)