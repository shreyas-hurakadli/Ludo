package com.shreyashurakadli.engine.state.position

interface PositionStatus {
    object Safe : PositionStatus
    object Unsafe : PositionStatus
}