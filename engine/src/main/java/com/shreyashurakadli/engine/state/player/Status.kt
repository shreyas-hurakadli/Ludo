package com.shreyashurakadli.engine.state.player

internal sealed interface Status {
    object Lost : Status
    object Won : Status
    object InProgress : Status
}