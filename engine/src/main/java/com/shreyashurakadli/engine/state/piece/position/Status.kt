package com.shreyashurakadli.engine.state.piece.position

internal interface Status {
    object Safe : Status
    object Unsafe : Status
}