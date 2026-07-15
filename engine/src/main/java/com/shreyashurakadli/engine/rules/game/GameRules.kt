package com.shreyashurakadli.engine.rules.game

import com.shreyashurakadli.engine.state.game.Game

internal interface GameRules {
    fun updateGameState(gameState: Game, diceValue: Int): Game
}