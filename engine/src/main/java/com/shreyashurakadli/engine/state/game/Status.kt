package com.shreyashurakadli.engine.state.game

internal sealed interface Status {
    object InProgress : Status
    object Finished : Status
}