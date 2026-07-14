package com.shreyashurakadli.engine.state.player

internal sealed interface PlayerStatus {
    object Won : PlayerStatus
    object InProgress : PlayerStatus
}