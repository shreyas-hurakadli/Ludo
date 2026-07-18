package com.shreyashurakadli.engine.state.game

sealed interface GameStatus {
    object InProgress : GameStatus
    object Finished : GameStatus
}