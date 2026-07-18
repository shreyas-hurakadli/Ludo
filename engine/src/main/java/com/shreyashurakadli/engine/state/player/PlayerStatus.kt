package com.shreyashurakadli.engine.state.player

sealed interface PlayerStatus {
    object Won : PlayerStatus
    object InProgress : PlayerStatus
}