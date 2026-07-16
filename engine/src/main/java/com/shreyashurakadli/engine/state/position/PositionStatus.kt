package com.shreyashurakadli.engine.state.position

internal interface PositionStatus {
    object Safe : PositionStatus
    object Unsafe : PositionStatus
}