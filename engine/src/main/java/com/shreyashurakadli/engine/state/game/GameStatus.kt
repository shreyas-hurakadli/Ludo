package com.shreyashurakadli.engine.state.game

internal sealed interface GameStatus {
    object InProgress : GameStatus
    object Finished : GameStatus
}