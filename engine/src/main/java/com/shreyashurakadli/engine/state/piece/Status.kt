package com.shreyashurakadli.engine.state.piece

internal interface Status {
    object InBase : Status
    object OnBoard : Status
    object Finished : Status
}