package com.shreyashurakadli.engine.state.position

@ConsistentCopyVisibility
data class Position internal constructor(
    val part: Int,
    val tile: Int,
    val status: PositionStatus
)