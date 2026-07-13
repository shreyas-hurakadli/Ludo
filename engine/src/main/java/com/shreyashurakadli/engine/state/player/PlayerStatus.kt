package com.shreyashurakadli.engine.state.player

internal sealed interface PlayerStatus {
    object Lost : PlayerStatus
    object Won : PlayerStatus
    object InProgress : PlayerStatus
}