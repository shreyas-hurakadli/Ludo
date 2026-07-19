package com.shreyashurakadli.engine.rules.position

import com.shreyashurakadli.engine.state.position.Position

internal interface PositionRules {
    fun updatePosition(state: Position, diceValue: Int, quadrantCount: Int): Position

    fun isAtBasePosition(tile: Int): Boolean
    fun isAtFinalPosition(tile: Int): Boolean
    fun provideBasePosition(): Position
}