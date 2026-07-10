package com.shreyashurakadli.engine.state.piece

internal interface Status {
    object InHome : Status
    object OnBoard : Status
    object Finished : Status
}