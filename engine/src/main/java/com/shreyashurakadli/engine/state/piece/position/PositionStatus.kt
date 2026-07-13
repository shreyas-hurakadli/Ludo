package com.shreyashurakadli.engine.state.piece.position

internal interface PositionStatus {
    object Safe : PositionStatus
    object Unsafe : PositionStatus
}